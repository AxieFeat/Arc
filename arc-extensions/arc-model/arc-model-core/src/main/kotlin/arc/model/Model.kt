package arc.model

import arc.Arc
import arc.annotations.ImmutableType
import arc.asset.StringAsset
import arc.model.animation.Animation
import arc.model.group.ElementGroup
import arc.model.texture.ModelTexture
import arc.util.pattern.Copyable
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
interface Model : Copyable<Model> {

    /**
     * All elements in this model.
     */
    val elements: MutableList<Element>

    /**
     * All groups in this model.
     */
    val groups: MutableList<ElementGroup>

    /**
     * All animations in this model.
     */
    val animations: MutableList<Animation>

    /**
     * All textures in this model.
     */
    val textures: MutableList<ModelTexture>

    /**
     * Merge this model with other model.
     *
     * @param model Model for merging.
     *
     * @return New instance of [Model].
     */
    fun merge(vararg model: Model): Model

    @ApiStatus.Internal
    interface Factory {

        fun create(elements: MutableList<Element>, groups: MutableList<ElementGroup>, animations: MutableList<Animation>, textures: MutableList<ModelTexture>): Model

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
        fun from(asset: StringAsset): Model {
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
            elements: MutableList<Element> = mutableListOf(),
            groups: MutableList<ElementGroup> = mutableListOf(),
            animations: MutableList<Animation> = mutableListOf(),
            textures: MutableList<ModelTexture> = mutableListOf(),
        ): Model {
            return Arc.factory<Factory>().create(elements, groups, animations, textures)
        }

    }

}