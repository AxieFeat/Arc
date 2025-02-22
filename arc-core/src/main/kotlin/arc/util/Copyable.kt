package arc.util

/**
 * Represents a general contract for copying objects of type [T].
 *
 * Classes implementing this interface are expected to provide a mechanism
 * for creating a deep copy or clone of an object instance of type [T].
 *
 * The `copy` method ensures that the returned object is distinct from the
 * original instance, sharing no references for mutable properties, thereby
 * preserving immutability or isolation where applicable.
 *
 * @param T The type of object that can be copied through this interface.
 */
interface Copyable<T> {

    /**
     * Creates and returns a new instance of the same type with identical properties.
     *
     * This method provides a mechanism for cloning or creating a deep copy
     * of an object, ensuring immutability or isolation in specific contexts.
     *
     * @return A new instance of the same type with the same properties as the original.
     */
    fun copy(): T

}