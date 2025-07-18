package arc.audio

import org.joml.Vector3f

/**
 * Represents a sound that can be played, stopped, and managed.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface Sound {

    /**
     * Indicates whether the sound is currently playing.
     *
     * This property provides a boolean value:
     * - `true` if the sound is actively being played.
     * - `false` if the sound is stopped or has not started playing.
     */
    @get:JvmName("isPlaying")
    val isPlaying: Boolean

    /**
     * Indicates whether the sound is currently paused.
     */
    @get:JvmName("isPaused")
    val isPaused: Boolean

    /**
     * Volume value of this sound.
     */
    var volume: Float

    /**
     * Pitch value of this sound.
     */
    var pitch: Float

    /**
     * Position value of this sound.
     */
    var position: Vector3f

    /**
     * Is this sound looping.
     */
    @get:JvmName("isLoop")
    @set:JvmName("setLoop")
    var isLoop: Boolean

    /**
     * Play this sound. If this sound already playing it will be restarted.
     *
     * @param volume Volume of sound.
     * @param pitch Pitch of sound.
     * @param position Position for playing.
     * @param loop Loop sound?
     * @param end Will be called after ending playing this sound.
     */
    fun play(volume: Float = this.volume, pitch: Float = this.pitch, position: Vector3f = this.position, loop: Boolean = this.isLoop, end: Sound.() -> Unit = {})

    /**
     * Stops the currently playing sound.
     * If no sound is playing, this method has no effect.
     */
    fun stop()

    /**
     * Pause playing sound.
     */
    fun pause()

}