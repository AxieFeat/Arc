package arc.audio

/**
 * This interface represents sound engine.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface SoundEngine {

    /**
     * Currently playing sounds.
     */
    @get:JvmName("playing")
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

}