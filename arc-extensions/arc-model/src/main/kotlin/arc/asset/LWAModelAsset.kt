package arc.asset

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * Represents a model asset of LWAModel format (Lightweight Arc Model format).
 */
@ImmutableType
interface LWAModelAsset : Asset {

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(file: File): LWAModelAsset

    }

    companion object {

        /**
         * Create instance of [LWAModelAsset] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [LWAModelAsset].
         */
        @JvmStatic
        fun from(file: File): LWAModelAsset {
            return Arc.factory<Factory>().create(file)
        }

    }

}