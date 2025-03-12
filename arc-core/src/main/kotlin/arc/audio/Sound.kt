package arc.audio

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.assets.SoundAsset
import arc.math.Point3d
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a sound that can be played, stopped, and managed.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
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
     * Represents the sound asset associated with this sound instance.
     */
    @get:JvmName("asset")
    val asset: SoundAsset

    /**
     * Play this sound. If this sound already playing it will be restarted.
     *
     * @param volume Volume of sound.
     * @param pitch Pitch of sound.
     * @param location Location for playing.
     * @param loop Loop sound?
     */
    fun play(volume: Float, pitch: Float, location: Point3d, loop: Boolean)

    /**
     * Stops the currently playing sound.
     * If no sound is playing, this method has no effect.
     */
    fun stop()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create the sound from some asset.
         *
         * @param asset Asset for sound.
         *
         * @return New instance of [Sound].
         */
        fun create(asset: SoundAsset): Sound

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
        fun from(asset: SoundAsset): Sound {
            return Arc.factory<Factory>().create(asset)
        }

    }

}