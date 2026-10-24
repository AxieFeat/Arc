package arc.window

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Ordered render-thread transport. One window owns native callbacks; listeners never install callbacks.
 */
class WindowEvents : AutoCloseable {

    private data class Listener(val priority: Int, val order: Long, val callback: (WindowEvent) -> Boolean)

    private val queue = ConcurrentLinkedQueue<WindowEvent>()
    private val listeners = mutableListOf<Listener>()
    private val input = WindowInputState()
    private var sequence = 0L
    private var order = 0L
    private var deferred = 0
    private var draining = false
    private var closed = false

    /**
     * Number of active input subscriptions.
     */
    val listenerCount get() = listeners.size

    /**
     * Reserves the next sequence number for an event from this window.
     */
    fun nextSequence() = ++sequence

    /**
     * Queues an event for the next ordered drain.
     */
    fun enqueue(event: WindowEvent) {
        check(!closed)
        queue += event
    }

    /**
     * Registers a listener; higher priorities run first and true consumes the event.
     */
    fun subscribe(priority: Int = 0, listener: (WindowEvent) -> Boolean): AutoCloseable {
        check(!closed)
        val entry = Listener(priority, ++order, listener)
        listeners += entry

        return AutoCloseable { listeners.remove(entry) }
    }

    /**
     * A UI lease defers delivery until its frame has current metrics/layout.
     */
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

    /**
     * Drains queued events unless a frame-delivery lease is active.
     */
    fun drainAutomatically() {
        if (deferred == 0) drain()
    }

    /**
     * Delivers queued events in order; recursive delivery is rejected.
     */
    fun drain() {
        check(!closed && !draining) { "Window input cannot be closed or recursively drained" }
        draining = true

        try {
            while (true) {
                val event = queue.poll() ?: break

                input.beforeDelivery(event)

                // A consumed press also suppresses repeats/releases below its consuming priority,
                // even if that listener/menu has since closed. Higher input layers still receive cleanup.
                val boundary = input.boundary(event)

                val consumer = deliver(event, boundary)
                input.afterDelivery(event, boundary, consumer?.let { WindowInputState.Boundary(it.priority, it.order) })
            }
        } finally {
            draining = false
        }
    }

    private fun deliver(event: WindowEvent, boundary: WindowInputState.Boundary?): Listener? {
        val ordered = listeners.sortedWith(compareByDescending<Listener> { it.priority }.thenBy { it.order })

        for (entry in ordered) {
            // A preceding callback may have removed a listener or closed the whole transport.
            if (entry in listeners && boundary?.allows(entry.priority, entry.order) != false && entry.callback(event)) {
                return entry
            }
        }

        return null
    }

    /**
     * Reports a held key only when its gesture is available to gameplay.
     */
    fun gameplayKeyDown(physicalCode: Int) = input.gameplayKeyDown(physicalCode)

    /**
     * Reports a held pointer button only when its gesture is available to gameplay.
     */
    fun gameplayButtonDown(button: Int) = input.gameplayButtonDown(button)

    /**
     * Masks currently held gestures until release without synthesizing input events.
     */
    fun maskAll(priority: Int = 0) {
        input.maskAll(priority)
    }

    override fun close() {
        if (closed) return

        closed = true
        queue.clear()
        listeners.clear()
        input.clear()
    }
}
