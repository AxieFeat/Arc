package arc.util.provider

import kotlin.jvm.Throws

/**
 * Used to provide various objects from the backend for static functions.
 */
interface ObjectProvider {

    /**
     * Provides the single object with the given type [type], or throws a
     * [TypeNotFoundException] if there is no object registered for the
     * given type.
     *
     * @param T The object type.
     * @param type the class of the type.
     *
     * @return The object.
     *
     * @throws TypeNotFoundException If there is no object registered for the given type.
     */
    @Throws(TypeNotFoundException::class)
    fun <T> provideSingle(type: Class<T>): T

    /**
     * Provides the factory of objects with the given type [type], or throws a
     * [TypeNotFoundException] if there is no factory of objects registered for the
     * given type.
     *
     * @param T The factory type.
     * @param type the class of the type.
     *
     * @return The object.
     *
     * @throws TypeNotFoundException If there is no factory registered for the given type.
     */
    fun <T> provideFactory(type: Class<T>): T

    /**
     * Registers the given single [obj] of the given [type] to this object provider.
     *
     * @param T The object type.
     * @param type The class of the type.
     * @param obj The object to register.
     * @param overwrite If an object already registers, it will be overwritten by a new object.
     *
     * @throws IllegalStateException If the object is already registered (Only if [overwrite] is false).
     */
    @Throws(IllegalStateException::class)
    fun <T> register(type: Class<T>, obj: T, overwrite: Boolean = true)

    /**
     * Register the given factory [factory] of the given [type] to this object provider.
     *
     * @param T The object type.
     * @param type The class of the type.
     * @param factory The factory of objects to register.
     * @param overwrite If an object factory already registers, it will be overwritten by a new provider.
     *
     * @throws IllegalStateException If the provider is already registered (Only if [overwrite] is false).
     */
    @Throws(IllegalStateException::class)
    fun <T> register(type: Class<T>, factory: () -> T, overwrite: Boolean = true)

}
