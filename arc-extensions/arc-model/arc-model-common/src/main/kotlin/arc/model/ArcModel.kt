package arc.model

import arc.model.animation.Animation
import arc.model.group.ElementGroup
import arc.model.texture.ModelTexture

internal data class ArcModel(
    override val elements: MutableList<Element> = mutableListOf(),
    override val groups: MutableList<ElementGroup> = mutableListOf(),
    override val animations: MutableList<Animation> = mutableListOf(),
    override val textures: MutableList<ModelTexture> = mutableListOf(),
) : Model {

    override fun merge(vararg model: Model): Model {
        return ArcModel(
            elements.toMutableList().also { list -> model.map { it.elements }.forEach { list.addAll(it) } },
            groups.toMutableList().also { list -> model.map { it.groups }.forEach { list.addAll(it) } },
            animations.toMutableList().also { list -> model.map { it.animations }.forEach { list.addAll(it) } },
            textures.toMutableList().also { list -> model.map { it.textures }.forEach { list.addAll(it) } },
        )
    }

    override fun copy(): Model {
        return ArcModel(elements.toMutableList(), groups.toMutableList(), animations.toMutableList(), textures.toMutableList())
    }

    object Factory : Model.Factory {
        override fun create(
            elements: MutableList<Element>,
            groups: MutableList<ElementGroup>,
            animations: MutableList<Animation>,
            textures: MutableList<ModelTexture>,
        ): Model {
            return ArcModel(elements, groups, animations, textures)
        }

    }

}