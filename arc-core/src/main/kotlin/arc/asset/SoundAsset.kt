package arc.asset

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * Represents a sound asset in the engine.
 */
@ImmutableType
interface SoundAsset : Asset {

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create instance of [SoundAsset] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [SoundAsset].
         */
        fun create(file: File): SoundAsset

    }

    companion object {

        /**
         * Create instance of [SoundAsset] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [SoundAsset].
         */
        @JvmStatic
        fun from(file: File): SoundAsset {
            return Arc.factory<Factory>().create(file)
        }

    }

}