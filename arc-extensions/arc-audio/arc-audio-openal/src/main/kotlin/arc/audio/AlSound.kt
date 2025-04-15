package arc.audio

import arc.math.Point3d
import org.lwjgl.openal.AL10.*
import java.nio.ShortBuffer

/**
 * Abstract OpenAL sound. Created to make it easy to create sounds of different formats
 */
internal class AlSound(
    private val pcm: ShortBuffer,
    private val format: Int,
    private val sampleRate: Int,
): Sound {

    private val source: Int = alGenSources().apply {
        alGenBuffers().also {
            alBufferData(it, format, pcm, sampleRate)
            alSourcei(this, AL_BUFFER, it)
        }
    }

    override var isPaused: Boolean = false
    override var isPlaying: Boolean = false

    override var volume: Float = 1.0f
        set(value) {
            field = value
            alSourcef(source, AL_GAIN, field)
        }

    override var pitch: Float = 1.0f
        set(value) {
            field = value
            alSourcef(source, AL_PITCH, field)
        }

    override var position: Point3d = Point3d.ZERO
        set(value) {
            field = value
            alSource3f(source, AL_POSITION, field.x.toFloat(), field.y.toFloat(), field.z.toFloat())
        }

    override var isLoop: Boolean = false
        set(value) {
            field = value
            alSourcei(source, AL_LOOPING, if (field) AL_TRUE else AL_FALSE)
        }

    override fun play(volume: Float, pitch: Float, position: Point3d, loop: Boolean, end: Sound.() -> Unit) {
        this.volume = volume
        this.pitch = pitch
        this.position = position
        this.isLoop = loop

        alSourcePlay(source)
        isPlaying = true

        Thread {
            while (isPlaying) {
                val state = alGetSourcei(source, AL_SOURCE_STATE)
                if (state != AL_PLAYING) {
                    isPlaying = false
                    end()
                    break
                }
                Thread.sleep(10)
            }
        }.start()
    }

    override fun stop() {
        if (!isPlaying) return

        alSourceStop(source)
        isPlaying = false
    }

    override fun pause() {
        if (!isPlaying) return

        alSourcePause(source)
        isPaused = true
    }
}