package arc.assets

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * Represents a sound asset in the system. This interface extends the [Asset] interface,
 * providing specific functionality for handling sound-related resources.
 *
 * Sound assets are immutable and are associated with underlying files that cannot be modified
 * after creation. To create a new instance of a SoundAsset, the companion object provides
 * the `from` method.
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