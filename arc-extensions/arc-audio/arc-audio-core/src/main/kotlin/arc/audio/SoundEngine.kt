package arc.audio

import arc.Arc.single
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents sound engine.
 */
interface SoundEngine {

    /**
     * Currently playing sounds.
     */
    val playing: Set<Sound>

    /**
     * Is sound engine loaded.
     */
    val isLoaded: Boolean

    /**
     * Starts the engine. It will be set [isLoaded] to `true`
     */
    fun start()

    /**
     * Stop engine. It will be set [isLoaded] to `false`
     */
    fun stop()

    /**
     * Stop playing of all sounds.
     */
    fun stopAll() {
        playing.forEach { it.stop() }
    }

    /**
     * Pause playing of all sounds.
     */
    fun pauseAll() {
        playing.forEach { it.pause() }
    }

    /**
     * Play some sound.
     */
    fun play(sound: Sound)

    /**
     * Stop some sound.
     */
    fun stop(sound: Sound)

    /**
     * Pause some sound.
     */
    fun pause(sound: Sound)

    @ApiStatus.Internal
    interface Provider {

        fun provide(): SoundEngine

    }

    companion object {

        /**
         * Find specific implementation of [SoundEngine] in current context.
         *
         * @return Instance of [SoundEngine].
         */
        @JvmStatic
        fun find(): SoundEngine {
            return single<Provider>().provide()
        }

    }

}