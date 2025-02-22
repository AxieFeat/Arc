package arc.audio

import arc.assets.SoundAsset
import arc.math.Point3i

data class ArcSound(
    override val asset: SoundAsset
) : Sound {

    override var isPlaying: Boolean = false

    override fun play(volume: Float, pitch: Float, location: Point3i, loop: Boolean) {

    }

    override fun stop() {

    }

    object Factory : Sound.Factory {
        override fun create(asset: SoundAsset): Sound {
            return ArcSound(asset)
        }

    }

}