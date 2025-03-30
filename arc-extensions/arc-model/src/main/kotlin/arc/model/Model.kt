package arc.model

import arc.annotations.ImmutableType
import arc.model.animation.Animation
import arc.model.group.ElementGroup
import arc.model.texture.ModelTexture

/**
 * Represents a 3D model with associated assets, elements, and textures.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Model {

    /**
     * All elements in this model.
     */
    @get:JvmName("elements")
    val elements: List<Element>

    /**
     * All groups in this model.
     */
    @get:JvmName("groups")
    val groups: List<ElementGroup>

    /**
     * All animations in this model.
     */
    @get:JvmName("animations")
    val animations: List<Animation>

    /**
     * All textures in this model.
     */
    @get:JvmName("textures")
    val textures: List<ModelTexture>

}