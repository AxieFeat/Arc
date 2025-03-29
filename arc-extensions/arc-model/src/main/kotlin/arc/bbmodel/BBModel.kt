package arc.bbmodel

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.asset.BBModelAsset
import arc.model.Model
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represent model in BB model format (Blockbench model format).
 */
@ImmutableType
interface BBModel : Model {

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(asset: BBModelAsset): BBModel

    }

    companion object {

        /**
         * Create [BBModel] from [BBModelAsset].
         *
         * @param asset Asset for Model.
         *
         * @return New instance of [BBModel].
         */
        @JvmStatic
        fun from(asset: BBModelAsset): BBModel {
            return Arc.factory<Factory>().create(asset)
        }

    }

}