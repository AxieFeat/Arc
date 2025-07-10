package arc.model

import arc.graphics.SimpleModelHandler
import arc.graphics.ModelHandler
import arc.model.animation.*
import arc.model.cube.SimpleCube
import arc.model.cube.SimpleCubeFace
import arc.model.cube.Cube
import arc.model.cube.CubeFace
import arc.model.group.SimpleElementGroup
import arc.model.group.ElementGroup
import arc.model.texture.SimpleModelTexture
import arc.model.texture.ModelTexture
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
        // Parts of model.
        provider.register<Model.Builder>({ SimpleModel.Builder() })
        provider.register<Cube.Builder>({ SimpleCube.Builder() })
        provider.register<CubeFace.Builder>({ SimpleCubeFace.Builder() })
        provider.register<ModelTexture.Builder>({ SimpleModelTexture.Builder() })
        provider.register<ElementGroup.Builder>({ SimpleElementGroup.Builder() })
        provider.register<Animation.Builder>({ SimpleAnimation.Builder() })
        provider.register<Animator.Builder>({ SimpleAnimator.Builder() })
        provider.register<Keyframe.Builder>({ SimpleKeyframe.Builder() })

        // Render model.
        provider.register<ModelHandler.Factory>(SimpleModelHandler.Factory)
    }

}