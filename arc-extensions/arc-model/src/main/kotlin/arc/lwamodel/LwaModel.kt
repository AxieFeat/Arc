package arc.lwamodel

import arc.Arc
import arc.annotations.ImmutableType
import arc.asset.LWAModelAsset
import arc.lwamodel.animation.LwamAnimation
import arc.lwamodel.group.LwamElementGroup
import arc.lwamodel.texture.LwamTexture
import arc.model.Model
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
interface LwaModel : Model {

    /**
     * All LWAM elements in this model.
     */
    override val elements: List<LwamElement>

    /**
     * All LWAM groups in this model.
     */
    override val groups: List<LwamElementGroup>

    /**
     * All LWAM animations in this model.
     */
    override val animations: List<LwamAnimation>

    /**
     * All LWAM textures in this model.
     */
    override val textures: List<LwamTexture>

    /**
     * Serialize model to bytes.
     */
    fun serialize(): ByteArray

    @ApiStatus.Internal
    interface Factory {

        fun create(bytes: ByteArray): LwaModel

        fun create(elements: List<LwamElement>, groups: List<LwamElementGroup>, animations: List<LwamAnimation>, textures: List<LwamTexture>): LwaModel

    }

    companion object {

        /**
         * Create [LwaModel] from [LWAModelAsset].
         *
         * @param asset Asset for Model.
         *
         * @return New instance of [LwaModel].
         */
        @JvmStatic
        fun from(asset: LWAModelAsset): LwaModel {
            return Arc.factory<Factory>().create(asset.file.readBytes())
        }

        /**
         * Create new instance of [LwaModel].
         *
         * @param elements Elements of model.
         * @param textures Textures of model.
         * @param animations Animations of model.
         *
         * @return New instance of [LwaModel].
         */
        @JvmStatic
        fun of(
            elements: List<LwamElement> = listOf(),
            groups: List<LwamElementGroup> = listOf(),
            animations: List<LwamAnimation> = listOf(),
            textures: List<LwamTexture> = listOf(),
        ): LwaModel {
            return Arc.factory<Factory>().create(elements, groups, animations, textures)
        }

    }

}