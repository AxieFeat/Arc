package arc.audio.format

import arc.audio.Sound
import arc.audio.SoundFormat

internal object Mp3SoundLoader : AbstractSoundLoader(SoundFormat.MP3) {

    override fun load(bytes: ByteArray): Sound {
        TODO("Not yet implemented")
    }

}