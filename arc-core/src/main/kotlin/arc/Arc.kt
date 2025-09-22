package arc

import arc.util.provider.ObjectProvider

/**
 * Provides static utility for accessing backend implementations through a configured [ObjectProvider].
 */
object Arc {

    // Set by reflection.
    @JvmStatic
    internal var objectProvider: ObjectProvider? = null

    /**
     * Function for getting [ObjectProvider].
     *
     * Not use before object provider initialized.
     */
    @JvmStatic
    fun factoryProvider(): ObjectProvider = objectProvider
        ?: throw UnsupportedOperationException("Cound not find ObjectProvider. Please call ArcObjectProvider.install() for initializing.")

    /**
     * Get single instance of [T].
     *
     * @param T Type of object.
     *
     * @return Instance of [T].
     */
    @JvmStatic
    @JvmSynthetic
    inline fun <reified T> single(): T = factoryProvider().provideSingle(T::class.java)

    /**
     * Get factory instance of [T].
     *
     * @param T Type of factory.
     *
     * @return Instance of [T].
     */
    @JvmStatic
    @JvmSynthetic
    inline fun <reified T> factory(): T = factoryProvider().provideFactory(T::class.java)
}
