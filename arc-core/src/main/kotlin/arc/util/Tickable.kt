package arc.util

/**
 * This interface represents tickable object.
 */
interface Tickable {

    /**
     * Tick this object.
     *
     * @param delta Delta value in this frame.
     */
    fun tick(delta: Float)

}