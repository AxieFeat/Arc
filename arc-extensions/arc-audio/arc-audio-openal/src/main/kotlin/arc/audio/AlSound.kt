package arc.audio

import org.joml.Vector3f
import org.lwjgl.openal.AL10.AL_BUFFER
import org.lwjgl.openal.AL10.AL_FALSE
import org.lwjgl.openal.AL10.AL_GAIN
import org.lwjgl.openal.AL10.AL_LOOPING
import org.lwjgl.openal.AL10.AL_PITCH
import org.lwjgl.openal.AL10.AL_PLAYING
import org.lwjgl.openal.AL10.AL_POSITION
import org.lwjgl.openal.AL10.AL_SOURCE_STATE
import org.lwjgl.openal.AL10.AL_TRUE
import org.lwjgl.openal.AL10.alBufferData
import org.lwjgl.openal.AL10.alGenBuffers
import org.lwjgl.openal.AL10.alGenSources
import org.lwjgl.openal.AL10.alGetSourcei
import org.lwjgl.openal.AL10.alSource3f
import org.lwjgl.openal.AL10.alSourcePause
import org.lwjgl.openal.AL10.alSourcePlay
import org.lwjgl.openal.AL10.alSourceStop
import org.lwjgl.openal.AL10.alSourcef
import org.lwjgl.openal.AL10.alSourcei
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

    override var position: Vector3f = Vector3f()
        set(value) {
            field = value
            alSource3f(source, AL_POSITION, field.x, field.y, field.z)
        }

    override var isLoop: Boolean = false
        set(value) {
            field = value
            alSourcei(source, AL_LOOPING, if (field) AL_TRUE else AL_FALSE)
        }

    override fun play(volume: Float, pitch: Float, position: Vector3f, loop: Boolean, end: Sound.() -> Unit) {
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
                Thread.sleep(SOUND_STATUS_CHECK_INTERVAL)
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

    companion object {

        const val SOUND_STATUS_CHECK_INTERVAL = 10L // ms
    }
}
