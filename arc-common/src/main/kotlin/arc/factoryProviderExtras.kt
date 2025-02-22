@file:JvmSynthetic
package arc

import arc.util.factory.FactoryProvider
import arc.util.factory.TypeNotFoundException

/**
 * Provides the factory with the given type [T], or throws a
 * [TypeNotFoundException] if there is no factory registered for the given
 * type.
 *
 * @param T The factory type.
 */
@JvmSynthetic
inline fun <reified T> FactoryProvider.provide(): T = provide(T::class.java)

/**
 * Registers the given [factory] of the given type [T] to this factory
 * provider.
 *
 * @param T The factory type.
 * @param factory The factory to register.
 * @param overwrite If factory already register it will be overwritten by new factory.
 *
 * @throws IllegalStateException If the factory is already registered (Only if [overwrite] is false).
 */
@JvmSynthetic
inline fun <reified T> FactoryProvider.register(factory: T, overwrite: Boolean = true) {
    register(T::class.java, factory, overwrite)
}
