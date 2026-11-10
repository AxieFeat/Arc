package arc.audio.format

import arc.audio.Sound
import arc.audio.SoundFormat

internal object FlacSoundLoader : AbstractSoundLoader(SoundFormat.FLAC) {

    @Suppress("NotImplementedDeclaration") // Remove when will be implemented.
    override fun load(bytes: ByteArray): Sound {
        TODO("Not yet implemented")
    }
}
