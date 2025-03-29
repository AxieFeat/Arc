package arc

import arc.util.factory.FactoryProvider

/**
 * Provides static utility for accessing factories through a configured [FactoryProvider].
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