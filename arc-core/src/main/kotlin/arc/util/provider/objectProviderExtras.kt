@file:JvmSynthetic
package arc.util.provider

/**
 * Provides the object with the given type [T], or throws a
 * [TypeNotFoundException] if there is no object registered for the given
 * type.
 *
 * @param T The object type.
 *
 * @throws TypeNotFoundException If there is no object registered for the given type.
 */
@JvmSynthetic
@Throws(TypeNotFoundException::class)
inline fun <reified T> ObjectProvider.provideSingle(): T = provideSingle(T::class.java)

/**
 * Registers the given single [obj] of the given type [T] to this object provider.
 *
 * @param T The object type.
 * @param obj The object to register.
 * @param overwrite If an object already registers, it will be overwritten by a new object.
 *
 * @throws IllegalStateException If the object is already registered (Only if [overwrite] is false).
 */
@JvmSynthetic
@Throws(IllegalStateException::class)
inline fun <reified T> ObjectProvider.register(obj: T, overwrite: Boolean = true) {
    register(T::class.java, obj, overwrite)
}

/**
 * Register the given factory [factory] of the given type [T] to this object provider.
 *
 * @param T The object type.
 * @param factory The factory of objects to register.
 * @param overwrite If an object factory already registers, it will be overwritten by a new provider.
 *
 * @throws IllegalStateException If the provider is already registered (Only if [overwrite] is false).
 */
@JvmSynthetic
@Throws(IllegalStateException::class)
inline fun <reified T> ObjectProvider.register(noinline factory: () -> T, overwrite: Boolean = true) {
    register(T::class.java, factory, overwrite)
}

