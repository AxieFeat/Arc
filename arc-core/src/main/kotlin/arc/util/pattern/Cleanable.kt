package arc.util.pattern

/**
 * This interface represents a resource that can be released from memory.
 */
interface Cleanable : AutoCloseable {

    /**
     * Releases any internal resources or handles associated with this object.
     * Should be called when the resource is no longer needed.
     */
    fun cleanup()

    override fun close() {
        cleanup()
    }
}
