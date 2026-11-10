package arc

import arc.util.provider.ObjectProvider
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

/**
 * Provides static utility for accessing backend implementations through a configured [ObjectProvider].
 */
object Arc {

    @JvmStatic
    internal var objectProvider: ObjectProvider? = null

    /**
     * Installs the provider used by the engine API.
     */
    fun install(provider: ObjectProvider) {
        objectProvider = provider
    }

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
    inline fun <reified T : Any> single(): T = factoryProvider().provideSingle(T::class)

    /**
     * Get factory instance of [T].
     *
     * @param T Type of factory.
     *
     * @return Instance of [T].
     */
    @JvmStatic
    @JvmSynthetic
    inline fun <reified T : Any> factory(): T = factoryProvider().provideFactory(T::class)
}
