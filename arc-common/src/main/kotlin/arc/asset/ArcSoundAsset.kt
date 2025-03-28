package arc.asset

import java.io.File

internal data class ArcSoundAsset(
    override val file: File
) : SoundAsset {

    object Factory : SoundAsset.Factory {
        override fun create(file: File): SoundAsset {
            return ArcSoundAsset(file)
        }

    }

}