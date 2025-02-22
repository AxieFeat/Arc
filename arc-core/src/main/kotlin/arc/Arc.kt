package arc

import arc.util.factory.FactoryProvider

/**
 * Provides static utility for accessing factories through a configured [FactoryProvider].
 *
 * This object acts as a central point for registering and accessing factory objects,
 * enabling dependency injection or service locator patterns. The actual provision
 * of factories is handled by an internal [FactoryProvider] instance, which must
 * be initialized before use.
 *
 * Note: The [FactoryProvider] must be appropriately set prior to calling any methods
 * on this object.
 */
object Arc {

    // Set by reflection.
    @JvmStatic
    internal var factoryProvider: FactoryProvider? = null

    // Not use before factory provider initialized.
    @JvmStatic
    fun factoryProvider(): FactoryProvider = factoryProvider!!

    /**
     * Get factory of [T].
     *
     * @param T Type of object.
     *
     * @return Instance of [T].
     */
    @JvmStatic
    @JvmSynthetic
    inline fun <reified T> factory(): T = factoryProvider().provide(T::class.java)

}