package arc.input

import arc.util.provider.ObjectProvider
import arc.util.provider.register

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
    fun bootstrap(provider: ObjectProvider) {
        provider.register<InputEngine.Provider>(GlfwInputEngine.Provider)
    }
}
