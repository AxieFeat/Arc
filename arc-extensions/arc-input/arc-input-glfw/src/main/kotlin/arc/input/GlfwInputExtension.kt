package arc.input

import arc.util.factory.FactoryProvider
import arc.util.factory.register

/**
 * This object class represents `arc-input` extension.
 */
object GlfwInputExtension {

    /**
     * Bootstrap factories of `arc-input` extension.
     *
     * @param provider Provider for configuring.
     */
    @JvmStatic
    fun bootstrap(provider: FactoryProvider) {
        provider.register<InputEngine.Factory>(GlfwInputEngine.Factory)
    }

}