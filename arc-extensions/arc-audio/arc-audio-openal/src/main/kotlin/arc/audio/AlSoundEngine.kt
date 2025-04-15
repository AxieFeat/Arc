package arc.audio

import org.lwjgl.openal.AL
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10.*
import org.lwjgl.system.MemoryUtil.NULL
import java.nio.ByteBuffer

internal object AlSoundEngine : SoundEngine {

    override val playing: MutableSet<Sound> = mutableSetOf()
    override var isLoaded: Boolean = false

    private var device: Long = NULL
    private var context: Long = NULL

    override fun start() {
        if (isLoaded) return

        device = alcOpenDevice(null as ByteBuffer?)
        if (device == NULL) {
            throw IllegalStateException("Failed to open OpenAL device")
        }

        context = alcCreateContext(device, intArrayOf(ALC_REFRESH, 60, 0))
        alcMakeContextCurrent(context)
        AL.createCapabilities(ALC.createCapabilities(device))

        isLoaded = true
    }

    override fun stop() {
        if (!isLoaded) return

        alcMakeContextCurrent(NULL)
        alcDestroyContext(context)
        alcCloseDevice(device)

        isLoaded = false
    }

    override fun stop(sound: Sound) {
        sound.stop()
        playing.remove(sound)
    }

    override fun play(sound: Sound) {
        sound.play { playing.remove(this) }
        playing.add(sound)
    }

    override fun pause(sound: Sound) {
        sound.pause()
    }

    object Factory : SoundEngine.Factory {
        override fun create(): SoundEngine {
            return AlSoundEngine
        }
    }

}