package arc.audio

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.asset.AssetLike
import arc.math.Point3d
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a sound that can be played, stopped, and managed.
 */
@MutableType
interface Sound {

    /**
     * Indicates whether the sound is currently playing.
     *
     * This property provides a boolean value:
     * - `true` if the sound is actively being played.
     * - `false` if the sound is stopped or has not started playing.
     */
    val isPlaying: Boolean

    /**
     * Indicates whether the sound is currently paused.
     */
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
    var position: Point3d

    /**
     * Is this sound looping.
     */
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
    fun play(volume: Float = this.volume, pitch: Float = this.pitch, position: Point3d = this.position, loop: Boolean = this.isLoop, end: Sound.() -> Unit = {})

    /**
     * Stops the currently playing sound.
     * If no sound is playing, this method has no effect.
     */
    fun stop()

    /**
     * Pause playing sound.
     */
    fun pause()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(bytes: ByteArray): Sound

    }

    companion object {

        /**
         * Create the sound from some asset.
         *
         * @param asset Asset for sound.
         *
         * @return New instance of [Sound].
         */
        @JvmStatic
        fun from(asset: AssetLike): Sound = from(asset.bytes)

        /**
         * Create the sound from array of bytes.
         *
         * @param bytes Bytes of sound.
         *
         * @return New instance of [Sound].
         */
        fun from(bytes: ByteArray): Sound {
            return Arc.factory<Factory>().create(bytes)
        }

    }

}