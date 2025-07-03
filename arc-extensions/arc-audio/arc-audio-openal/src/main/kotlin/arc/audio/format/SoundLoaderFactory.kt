package arc.audio.format

import arc.audio.SoundFormat
import arc.audio.SoundLoader

internal object SoundLoaderFactory : SoundLoader.Factory {

    override fun create(format: SoundFormat): SoundLoader {
        return when (format) {
            SoundFormat.OGG -> OggSoundLoader
            SoundFormat.WAV -> WavSoundLoader
            SoundFormat.MP3 -> Mp3SoundLoader
            SoundFormat.FLAC -> FlacSoundLoader
        }
    }

}