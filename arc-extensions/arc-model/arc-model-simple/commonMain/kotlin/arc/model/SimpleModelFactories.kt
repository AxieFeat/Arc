package arc.model

import arc.model.animation.*
import arc.model.cube.*
import arc.model.group.*
import arc.model.texture.*
import arc.util.provider.ObjectProvider
import arc.util.provider.register

/**
 * Registers the portable model builders with the supplied provider.
 */
object SimpleModelFactories {
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

    }
}
