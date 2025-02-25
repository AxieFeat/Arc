package arc.assets

import arc.Arc
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * This interface represents asset of font.
 */
interface FontAsset : Asset {

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create instance of [FontAsset] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [FontAsset].
         */
        fun create(file: File): FontAsset

    }

    companion object {

        /**
         * Create instance of [FontAsset] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [FontAsset].
         */
        @JvmStatic
        fun from(file: File): FontAsset {
            return Arc.factory<Factory>().create(file)
        }

    }

}