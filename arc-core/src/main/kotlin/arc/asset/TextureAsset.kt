package arc.asset

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * This interface represents texture asset in engine.
 */
@ImmutableType
interface TextureAsset : Asset {

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(file: File): TextureAsset

    }

    companion object {

        /**
         * Create instance of [TextureAsset] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [TextureAsset].
         */
        @JvmStatic
        fun from(file: File): TextureAsset {
            return Arc.factory<Factory>().create(file)
        }

    }

}