package arc.lwamodel

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.asset.BBModelAsset
import arc.asset.LWAModelAsset
import arc.bbmodel.BBModel
import arc.lwamodel.animation.LWAModelAnimation
import arc.lwamodel.texture.LWAModelTexture
import arc.model.Model
import arc.model.animation.Animation
import arc.model.texture.ModelTexture
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents model in LWA model format.
 *
 * LWA Model (Lightweight Arc Model) - its format of models, that based on BB Model format, but:
 *  * Any method for storaging, it can be binary file also.
 *  * Without unnecessary parameters, only those necessary for rendering.
 *  * Support for colored lightning in elements.
 *
 * Due to its advantages over BB Model, this format is more preferable for use,
 * and it is also very good for transferring over the network.
 */
@ImmutableType
interface LWAModel : Model {

    /**
     * All LWA Model elements in this model.
     */
    override val elements: List<LWAModelElement>

    /**
     * All LWA Model textures in this model.
     */
    override val textures: List<LWAModelTexture>

    /**
     * All LWA Model animations in this model.
     */
    override val animations: List<LWAModelAnimation>

    fun serialize(): ByteArray

    @ApiStatus.Internal
    interface Factory {

        fun create(bytes: ByteArray): LWAModel

        fun create(elements: List<LWAModelElement>, textures: List<LWAModelTexture>, animations: List<LWAModelAnimation>): LWAModel

    }

    companion object {

        /**
         * Create [LWAModel] from [LWAModelAsset].
         *
         * @param asset Asset for Model.
         *
         * @return New instance of [LWAModel].
         */
        @JvmStatic
        fun from(asset: LWAModelAsset): LWAModel {
            return Arc.factory<Factory>().create(asset.file.readBytes())
        }

        /**
         * Create new instance of [LWAModel].
         *
         * @param elements Elements of model.
         * @param textures Textures of model.
         * @param animations Animations of model.
         *
         * @return New instance of [LWAModel].
         */
        @JvmStatic
        fun of(
            elements: List<LWAModelElement> = listOf(),
            textures: List<LWAModelTexture> = listOf(),
            animations: List<LWAModelAnimation> = listOf()
        ): LWAModel {
            return Arc.factory<Factory>().create(elements, textures, animations)
        }

    }

}