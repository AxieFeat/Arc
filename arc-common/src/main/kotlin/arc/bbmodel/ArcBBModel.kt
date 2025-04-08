package arc.bbmodel

import arc.model.Element
import arc.model.animation.Animation
import arc.model.group.ElementGroup
import arc.model.texture.ModelTexture

// TODO
internal data class ArcBBModel(
    override val elements: List<Element>,
    override val groups: List<ElementGroup>,
    override val animations: List<Animation>,
    override val textures: List<ModelTexture>
) : BBModel