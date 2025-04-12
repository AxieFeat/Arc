package arc.model

import arc.annotations.ImmutableType
import arc.model.animation.Animation
import arc.model.group.ElementGroup
import arc.model.texture.ModelTexture

/**
 * Represents a 3D model with associated assets, elements, and textures.
 */
@ImmutableType
interface Model {

    /**
     * All elements in this model.
     */
    val elements: List<Element>

    /**
     * All groups in this model.
     */
    val groups: List<ElementGroup>

    /**
     * All animations in this model.
     */
    val animations: List<Animation>

    /**
     * All textures in this model.
     */
    val textures: List<ModelTexture>

}