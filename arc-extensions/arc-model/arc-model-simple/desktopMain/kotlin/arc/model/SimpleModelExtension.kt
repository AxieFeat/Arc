package arc.model

import arc.graphics.SimpleModelHandler
import arc.graphics.ModelHandler
import arc.util.provider.ObjectProvider
import arc.util.provider.register

/**
 * This object class represents factory provider for models `arc-model` extension.
 */
object SimpleModelExtension {

    /**
     * Bootstrap factories of `arc-model` extension.
     *
     * @param provider Provider for configuring.
     */
    @JvmStatic
    fun bootstrap(provider: ObjectProvider) {
        SimpleModelFactories.bootstrap(provider)

        // Render model.
        provider.register<ModelHandler.Factory>(SimpleModelHandler.Factory)
    }

}
