package arc.audio.format

import arc.audio.AlSound
import arc.audio.Sound
import arc.audio.SoundFormat
import org.lwjgl.openal.AL10.AL_FORMAT_MONO16
import org.lwjgl.openal.AL10.AL_FORMAT_STEREO16
import org.lwjgl.stb.STBVorbis
import org.lwjgl.stb.STBVorbisInfo
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.ShortBuffer

internal object OggSoundLoader : AbstractSoundLoader(SoundFormat.OGG) {

    override fun load(bytes: ByteArray): Sound {
        val soundBytes: ByteBuffer = MemoryUtil.memAlloc(bytes.size).put(bytes)
        soundBytes.flip()

        MemoryStack.stackPush().use { stack ->
            val error: IntBuffer = stack.mallocInt(1)
            val info = STBVorbisInfo.malloc(stack)

            val decoder = STBVorbis.stb_vorbis_open_memory(soundBytes, error, null)

            STBVorbis.stb_vorbis_get_info(decoder, info)

            val channels = info.channels()
            val sampleRate = info.sample_rate()
            val samples = STBVorbis.stb_vorbis_stream_length_in_samples(decoder)

            val pcm: ShortBuffer = MemoryUtil.memAllocShort(samples * channels)
            STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm)
            STBVorbis.stb_vorbis_close(decoder)

            val format = if (channels == 1) AL_FORMAT_MONO16 else AL_FORMAT_STEREO16
            return AlSound(pcm, format, sampleRate)
        }
    }
}