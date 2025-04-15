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
        provider.register<Model.Factory>(ArcModel.Factory)
        provider.register<Cube.Factory>(ArcCube.Factory)
        provider.register<CubeFace.Factory>(ArcCubeFace.Factory)
        provider.register<ModelTexture.Factory>(ArcModelTexture.Factory)
        provider.register<ElementGroup.Factory>(ArcElementGroup.Factory)
        provider.register<Animation.Factory>(ArcAnimation.Factory)
        provider.register<Animator.Factory>(ArcAnimator.Factory)
        provider.register<Keyframe.Factory>(ArcKeyframe.Factory)

        // Render model.
        provider.register<ModelHandler.Factory>(ArcModelHandler.Factory)
    }

}