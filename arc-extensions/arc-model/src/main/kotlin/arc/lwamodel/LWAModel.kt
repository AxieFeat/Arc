package arc.lwamodel

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.asset.BBModelAsset
import arc.asset.LWAModelAsset
import arc.bbmodel.BBModel
import arc.model.Model
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

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(bytes: ByteArray): LWAModel

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

    }

}