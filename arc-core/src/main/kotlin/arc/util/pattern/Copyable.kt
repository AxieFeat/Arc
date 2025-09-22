package arc.util.pattern

/**
 * Represents a general contract for copying objects of type [T].
 *
 * @param T The type of object that can be copied through this interface.
 */
interface Copyable<T> {

    /**
     * Creates and returns a new instance of the same type with identical properties.
     *
     * @return A new instance of the same type with the same properties as the original.
     */
    fun copy(): T
}
