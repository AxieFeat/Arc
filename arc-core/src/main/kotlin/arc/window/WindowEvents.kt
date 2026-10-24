package arc.window

import java.util.concurrent.ConcurrentLinkedQueue

data class WindowMetrics(
    val logicalWidth: Int, val logicalHeight: Int, val framebufferWidth: Int, val framebufferHeight: Int, val scaleX: Float = 1f,
    val scaleY: Float = 1f, val focused: Boolean = true, val minimized: Boolean = false, val generation: Long = 0
)

enum class KeyAction {
    PRESS,
    REPEAT,
    RELEASE
}
enum class CursorShape {
    DEFAULT,
    POINTER,
    TEXT,
    CROSSHAIR,
    RESIZE_HORIZONTAL,
    RESIZE_VERTICAL
}
enum class KeySymbol {
    TAB,
    ENTER,
    SPACE,
    ESCAPE,
    LEFT,
    RIGHT,
    UP,
    DOWN,
    HOME,
    END,
    BACKSPACE,
    DELETE,
    A,
    C,
    V,
    X,
    Y,
    Z,
    UNKNOWN
}
data class KeyModifiers(val shift: Boolean = false, val control: Boolean = false, val alt: Boolean = false, val meta: Boolean = false)
interface WindowEvent {

    val sequence: Long
    val timeNanos: Long
    val windowId: Long
}

data class PointerMotion(val x: Double, val y: Double, override val sequence: Long, override val timeNanos: Long, override val windowId: Long) :
    WindowEvent

data class PointerButton(
    val button: Int, val action: KeyAction, val x: Double, val y: Double, val modifiers: KeyModifiers, override val sequence: Long,
    override val timeNanos: Long, override val windowId: Long
) : WindowEvent

data class PointerWheel(
    val x: Double, val y: Double, val deltaX: Double, val deltaY: Double, override val sequence: Long, override val timeNanos: Long,
    override val windowId: Long
) : WindowEvent

data class KeyboardEvent(
    val symbol: KeySymbol, val physicalCode: Int, val scanCode: Int, val action: KeyAction, val modifiers: KeyModifiers, override val sequence: Long,
    override val timeNanos: Long, override val windowId: Long
) : WindowEvent

data class UnicodeCommit(val text: String, override val sequence: Long, override val timeNanos: Long, override val windowId: Long) : WindowEvent
data class FocusEvent(val focused: Boolean, override val sequence: Long, override val timeNanos: Long, override val windowId: Long) : WindowEvent
data class MetricsEvent(val metrics: WindowMetrics, override val sequence: Long, override val timeNanos: Long, override val windowId: Long) :
    WindowEvent

data class PointerBoundary(val entered: Boolean, override val sequence: Long, override val timeNanos: Long, override val windowId: Long) : WindowEvent

/** Ordered render-thread transport. One window owns native callbacks; listeners never install callbacks. */
class WindowEvents : AutoCloseable {

    private data class Listener(val priority: Int, val order: Long, val callback: (WindowEvent) -> Boolean)
    private data class InputBoundary(val priority: Int, val order: Long)

    private fun tighter(first: InputBoundary?, second: InputBoundary): InputBoundary =
        if (first != null && (first.priority > second.priority || first.priority == second.priority && first.order < second.order)) first else second

    private val queue = ConcurrentLinkedQueue<WindowEvent>()
    private val listeners = mutableListOf<Listener>()
    private val keys = mutableSetOf<Int>()
    private val buttons = mutableSetOf<Int>()
    private val blockedKeys = mutableMapOf<Int, InputBoundary>()
    private val blockedButtons = mutableMapOf<Int, InputBoundary>()
    private var sequence = 0L
    private var order = 0L
    private var deferred = 0
    private var draining = false
    private var closed = false
    val listenerCount get() = listeners.size
    fun nextSequence() = ++sequence
    fun enqueue(event: WindowEvent) {
        check(!closed)
        queue += event
    }

    fun subscribe(priority: Int = 0, listener: (WindowEvent) -> Boolean): AutoCloseable {
        check(!closed)
        val entry = Listener(priority, ++order, listener)
        listeners += entry
        return AutoCloseable { listeners.remove(entry) }
    }

    /** A UI lease defers delivery until its frame has current metrics/layout. */
    fun deferAutomaticDelivery(): AutoCloseable {
        check(!closed)
        deferred++
        var active = true
        return AutoCloseable {
            if (active) {
                active = false
                deferred--
            }
        }
    }

    fun drainAutomatically() {
        if (deferred == 0) drain()
    }

    fun drain() {
        check(!closed && !draining) { "Window input cannot be closed or recursively drained" }
        draining = true
        try {
            while (true) {
                val event = queue.poll() ?: break
                when (event) {
                    is KeyboardEvent -> if (event.action == KeyAction.RELEASE) {
                        keys -= event.physicalCode
                    } else if (event.physicalCode !in blockedKeys) keys += event.physicalCode

                    is PointerButton -> if (event.action == KeyAction.RELEASE) {
                        buttons -= event.button
                    } else if (event.button !in blockedButtons) buttons += event.button

                    is FocusEvent -> if (!event.focused) {
                        keys.clear()
                        buttons.clear()
                        blockedKeys.clear()
                        blockedButtons.clear()
                    }
                }
                // A consumed press also suppresses repeats/releases below its consuming priority,
                // even if that listener/menu has since closed. Higher input layers still receive cleanup.
                val boundary = when (event) {
                    is KeyboardEvent -> blockedKeys[event.physicalCode]
                    is PointerButton -> blockedButtons[event.button]
                    else -> null
                }
                var consumed = false
                var consumer: Listener? = null
                listeners.sortedWith(compareByDescending<Listener> { it.priority }.thenBy { it.order }).toList().forEach { entry ->
                    if (
                        !consumed && entry in listeners &&
                        (boundary == null || entry.priority > boundary.priority ||
                            entry.priority == boundary.priority && entry.order <= boundary.order)
                    ) {
                        consumed = entry.callback(event)
                        if (consumed) consumer = entry
                    }
                }
                when (event) {
                    is KeyboardEvent -> if (event.action == KeyAction.RELEASE) blockedKeys.remove(event.physicalCode) else if (consumed) {
                        val entry = checkNotNull(consumer)
                        blockedKeys[event.physicalCode] = tighter(boundary, InputBoundary(entry.priority, entry.order))
                        keys -= event.physicalCode
                    }

                    is PointerButton -> if (event.action == KeyAction.RELEASE) blockedButtons.remove(event.button) else if (consumed) {
                        val entry = checkNotNull(consumer)
                        blockedButtons[event.button] = tighter(boundary, InputBoundary(entry.priority, entry.order))
                        buttons -= event.button
                    }

                    is FocusEvent -> if (!event.focused) {
                        keys.clear()
                        buttons.clear()
                        blockedKeys.clear()
                        blockedButtons.clear()
                    }
                }
            }
        } finally {
            draining = false
        }
    }

    fun gameplayKeyDown(physicalCode: Int) = physicalCode in keys && physicalCode !in blockedKeys
    fun gameplayButtonDown(button: Int) = button in buttons && button !in blockedButtons
    fun maskAll(priority: Int = 0) {
        val boundary = InputBoundary(priority, Long.MAX_VALUE)
        keys.forEach { blockedKeys[it] = tighter(blockedKeys[it], boundary) }
        buttons.forEach { blockedButtons[it] = tighter(blockedButtons[it], boundary) }
        keys.clear()
        buttons.clear()
    }

    override fun close() {
        if (closed) return
        closed = true
        queue.clear()
        listeners.clear()
        keys.clear()
        buttons.clear()
        blockedKeys.clear()
        blockedButtons.clear()
    }
}
