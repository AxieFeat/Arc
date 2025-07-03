package arc.audio.format

import arc.audio.Sound
import arc.audio.SoundFormat

internal object FlacSoundLoader : AbstractSoundLoader(SoundFormat.FLAC) {

    override fun load(bytes: ByteArray): Sound {
        TODO("Not yet implemented")
    }

}