package arc.audio.format

import arc.audio.Sound
import arc.audio.SoundFormat

internal object WavSoundLoader : AbstractSoundLoader(SoundFormat.WAV) {

    override fun load(bytes: ByteArray): Sound {
        TODO("Not yet implemented")
    }

}