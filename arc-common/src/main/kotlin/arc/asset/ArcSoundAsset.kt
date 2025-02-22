package arc.asset

import arc.assets.SoundAsset
import java.io.File

data class ArcSoundAsset(
    override val file: File
) : SoundAsset {

    object Factory : SoundAsset.Factory {
        override fun create(file: File): SoundAsset {
            return ArcSoundAsset(file)
        }

    }

}