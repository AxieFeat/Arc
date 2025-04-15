package arc.model

import arc.Arc
import arc.annotations.ImmutableType
import arc.asset.StringAsset
import arc.model.animation.Animation
import arc.model.group.ElementGroup
import arc.model.texture.ModelTexture
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents model in LWAM format.
 *
 * LWAM (Lightweight Arc Model) - its format of models, that based on BB Model format, but:
 *  * Any method for storaging, it can be binary file also.
 *  * Without unnecessary parameters, only those necessary for rendering.
 *  * Support for colored lightning in elements.
 *
 * Due to its advantages over BB Model, this format is more preferable for use,
 * and it is also very good for transferring over the network.
 */
@ImmutableType
interface Model {

    /**
     * All elements in this model.
     */
    val elements: List<arc.model.Element>

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

    @ApiStatus.Internal
    interface Factory {

        fun create(elements: List<arc.model.Element>, groups: List<ElementGroup>, animations: List<Animation>, textures: List<ModelTexture>): arc.model.Model

    }

    companion object {

        /**
         * Create [Model] from [StringAsset].
         *
         * @param asset Asset for Model.
         *
         * @return New instance of [Model].
         */
        @JvmStatic
        fun from(asset: StringAsset): arc.model.Model {
            TODO()
        }

        /**
         * Create new instance of [Model].
         *
         * @param elements Elements of model.
         * @param textures Textures of model.
         * @param animations Animations of model.
         *
         * @return New instance of [Model].
         */
        @JvmStatic
        fun of(
            elements: List<arc.model.Element> = listOf(),
            groups: List<ElementGroup> = listOf(),
            animations: List<Animation> = listOf(),
            textures: List<ModelTexture> = listOf(),
        ): arc.model.Model {
            return Arc.factory<arc.model.Model.Factory>().create(elements, groups, animations, textures)
        }

    }

}