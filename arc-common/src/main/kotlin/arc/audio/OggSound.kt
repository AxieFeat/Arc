package arc.audio

import org.lwjgl.openal.AL10.*
import org.lwjgl.stb.STBVorbis
import org.lwjgl.stb.STBVorbisInfo
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.ShortBuffer

internal class OggSound(
    bytes: ByteArray,
) : AbstractSound() {

    private val buffer = alGenBuffers()

    init {
        val (pcm, format, sampleRate) = loadOgg(bytes)
        alBufferData(buffer, format, pcm, sampleRate)
        alSourcei(source, AL_BUFFER, buffer)
    }

    object Factory : Sound.Factory {
        override fun create(bytes: ByteArray): Sound {
            return OggSound(bytes)
        }
    }

    companion object {
        private fun loadOgg(data: ByteArray): Triple<ShortBuffer, Int, Int> {
            val bytes: ByteBuffer = MemoryUtil.memAlloc(data.size).put(data)
            bytes.flip()

            MemoryStack.stackPush().use { stack ->
                val error: IntBuffer = stack.mallocInt(1)
                val info = STBVorbisInfo.malloc(stack)

                val decoder = STBVorbis.stb_vorbis_open_memory(bytes, error, null)

                STBVorbis.stb_vorbis_get_info(decoder, info)

                val channels = info.channels()
                val sampleRate = info.sample_rate()
                val samples = STBVorbis.stb_vorbis_stream_length_in_samples(decoder)

                val pcm: ShortBuffer = MemoryUtil.memAllocShort(samples * channels)
                STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm)
                STBVorbis.stb_vorbis_close(decoder)

                val format = if (channels == 1) AL_FORMAT_MONO16 else AL_FORMAT_STEREO16
                return Triple(pcm, format, sampleRate)
            }
        }

    }
}