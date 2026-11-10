package arc.util.provider

import kotlin.reflect.KClass

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
    fun <T : Any> provideSingle(type: KClass<T>): T

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
    fun <T : Any> provideFactory(type: KClass<T>): T

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
    fun <T : Any> register(type: KClass<T>, obj: T, overwrite: Boolean = true)

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
    fun <T : Any> register(type: KClass<T>, factory: () -> T, overwrite: Boolean = true)

}
