package arc.model

import arc.ArcFactoryProvider
import arc.graphics.ArcModelHandler
import arc.graphics.ModelHandler
import arc.model.animation.*
import arc.model.animation.ArcAnimation
import arc.model.cube.ArcCube
import arc.model.cube.ArcCubeFace
import arc.model.cube.Cube
import arc.model.cube.CubeFace
import arc.model.group.ArcElementGroup
import arc.model.group.ElementGroup
import arc.model.texture.ArcModelTexture
import arc.model.texture.ModelTexture
import arc.util.factory.FactoryProvider
import arc.util.factory.register

/**
 * This object class represents factory provider for models `arc-model` extension.
 */
object CommonModelExtension {

    /**
     * Bootstrap factories of `arc-model` extension.
     *
     * @param provider Provider for configuring.
     */
    @JvmStatic
    fun bootstrap(provider: FactoryProvider = ArcFactoryProvider) {
        // Parts of model.
        provider.register<Model.Builder>({ ArcModel.Builder() })
        provider.register<Cube.Builder>({ ArcCube.Builder() })
        provider.register<CubeFace.Builder>({ ArcCubeFace.Builder() })
        provider.register<ModelTexture.Builder>({ ArcModelTexture.Builder() })
        provider.register<ElementGroup.Builder>({ ArcElementGroup.Builder() })
        provider.register<Animation.Builder>({ ArcAnimation.Builder() })
        provider.register<Animator.Builder>({ ArcAnimator.Builder() })
        provider.register<Keyframe.Builder>({ ArcKeyframe.Builder() })

        // Render model.
        provider.register<ModelHandler.Factory>(ArcModelHandler.Factory)
    }

}