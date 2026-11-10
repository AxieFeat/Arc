package arc.audio.format

import arc.audio.SoundFormat
import arc.audio.SoundLoader

internal abstract class AbstractSoundLoader(
    override val format: SoundFormat,
) : SoundLoader
