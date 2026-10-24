package arc.input

import arc.window.Window
import arc.window.WindowEvent

/** Ordered input priority and a masked gameplay polling view. Physical polling is separate. */
class InputContext private constructor(private val window: Window, val priority: Int) : AutoCloseable {

    private val subscriptions = mutableListOf<AutoCloseable>()
    private var closed = false
    fun subscribe(listener: (WindowEvent) -> Boolean): AutoCloseable {
        check(!closed)
        val subscription = window.events.subscribe(priority, listener)
        subscriptions += subscription
        return AutoCloseable { if (subscriptions.remove(subscription)) subscription.close() }
    }

    fun isPressed(key: KeyCode): Boolean = when (key.keyType) {
        KeyType.KEY -> window.events.gameplayKeyDown(key.id)
        KeyType.MOUSE -> window.events.gameplayButtonDown(key.id)
        else -> false
    }

    fun maskHeldInput() {
        check(!closed)
        window.events.maskAll(priority)
    }

    override fun close() {
        if (closed) return
        closed = true
        subscriptions.asReversed().forEach { it.close() }
        subscriptions.clear()
    }

    companion object {

        fun of(window: Window, priority: Int = 0) = InputContext(window, priority)
    }
}
