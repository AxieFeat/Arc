package arc.window

/**
 * Keeps consumed gestures masked until release, even after their listener has closed.
 */
internal class WindowInputState {

    data class Boundary(val priority: Int, val order: Long) {

        fun allows(priority: Int, order: Long): Boolean = when {
            priority > this.priority -> true
            priority < this.priority -> false
            else -> order <= this.order
        }
    }

    private val keys = mutableSetOf<Int>()
    private val buttons = mutableSetOf<Int>()
    private val blockedKeys = mutableMapOf<Int, Boundary>()
    private val blockedButtons = mutableMapOf<Int, Boundary>()

    fun beforeDelivery(event: WindowEvent) {
        when (event) {
            is KeyboardEvent -> if (event.action == KeyAction.RELEASE) {
                keys -= event.physicalCode
            } else if (event.physicalCode !in blockedKeys) {
                keys += event.physicalCode
            }

            is PointerButton -> if (event.action == KeyAction.RELEASE) {
                buttons -= event.button
            } else if (event.button !in blockedButtons) {
                buttons += event.button
            }

            is FocusEvent -> if (!event.focused) clear()
        }
    }

    fun boundary(event: WindowEvent): Boundary? = when (event) {
        is KeyboardEvent -> blockedKeys[event.physicalCode]
        is PointerButton -> blockedButtons[event.button]
        else -> null
    }

    fun afterDelivery(event: WindowEvent, boundary: Boundary?, consumer: Boundary?) {
        when (event) {
            is KeyboardEvent -> if (event.action == KeyAction.RELEASE) {
                blockedKeys.remove(event.physicalCode)
            } else if (consumer != null) {
                blockedKeys[event.physicalCode] = tighter(boundary, consumer)
                keys -= event.physicalCode
            }

            is PointerButton -> if (event.action == KeyAction.RELEASE) {
                blockedButtons.remove(event.button)
            } else if (consumer != null) {
                blockedButtons[event.button] = tighter(boundary, consumer)
                buttons -= event.button
            }

            is FocusEvent -> if (!event.focused) clear()
        }
    }

    fun gameplayKeyDown(physicalCode: Int) = physicalCode in keys && physicalCode !in blockedKeys

    fun gameplayButtonDown(button: Int) = button in buttons && button !in blockedButtons

    fun maskAll(priority: Int) {
        val boundary = Boundary(priority, Long.MAX_VALUE)
        keys.forEach { blockedKeys[it] = tighter(blockedKeys[it], boundary) }
        buttons.forEach { blockedButtons[it] = tighter(blockedButtons[it], boundary) }
        keys.clear()
        buttons.clear()
    }

    fun clear() {
        keys.clear()
        buttons.clear()
        blockedKeys.clear()
        blockedButtons.clear()
    }

    private fun tighter(first: Boundary?, second: Boundary): Boundary = when {
        first == null -> second
        first.priority > second.priority -> first
        first.priority < second.priority -> second
        first.order < second.order -> first
        else -> second
    }
}
