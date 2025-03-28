package arc.asset

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * Represents a model asset of LWAModel format (Lightweight Arc Model format).
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface LWAModelAsset : Asset {

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create instance of [BBModelAsset] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [BBModelAsset].
         */
        fun create(file: File): BBModelAsset

    }

    companion object {

        /**
         * Create instance of [BBModelAsset] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [BBModelAsset].
         */
        @JvmStatic
        fun from(file: File): BBModelAsset {
            return Arc.factory<Factory>().create(file)
        }

    }

}