package arc.model

import arc.model.animation.Animation
import arc.model.group.ElementGroup
import arc.model.texture.ModelTexture

internal data class ArcModel(
    override val elements: List<Element> = listOf(),
    override val groups: List<ElementGroup> = listOf(),
    override val animations: List<Animation> = listOf(),
    override val textures: List<ModelTexture> = listOf(),
) : Model {

    object Factory : Model.Factory {
        override fun create(
            elements: List<Element>,
            groups: List<ElementGroup>,
            animations: List<Animation>,
            textures: List<ModelTexture>,
        ): Model {
            return ArcModel(elements, groups, animations, textures)
        }

    }

}