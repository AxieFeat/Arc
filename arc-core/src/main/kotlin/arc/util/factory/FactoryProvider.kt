@file:JvmSynthetic
package arc.util.factory

/**
 * Used to provide various factories from the backend for static factory
 * functions.
 */
interface FactoryProvider {

    /**
     * Provides the factory with the given type [type], or throws a
     * [TypeNotFoundException] if there is no factory registered for the
     * given type.
     *
     * @param T The factory type.
     * @param type the class of the type.
     * @return The factory.
     */
    fun <T> provide(type: Class<T>): T

    /**
     * Registers the given [factory] of the given [type] to this factory
     * provider.
     *
     * @param T The factory type.
     * @param type The class of the type.
     * @param factory The factory to register.
     * @param overwrite If factory already register it will be overwritten by new factory.
     *
     * @throws IllegalStateException If the factory is already registered (Only if [overwrite] is false).
     */
    fun <T> register(type: Class<T>, factory: T, overwrite: Boolean = true)

}
