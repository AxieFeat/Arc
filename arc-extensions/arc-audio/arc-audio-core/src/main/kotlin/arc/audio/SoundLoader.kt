package arc.audio

import arc.Arc.single
import arc.annotations.TypeFactory
import arc.asset.AssetLike
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents loader for sounds in specific format.
 */
interface SoundLoader {

    /**
     * Format of sounds.
     */
    val format: SoundFormat

    /**
     * Load sound from bytes.
     *
     * @param bytes Bytes of sound.
     *
     * @return Instance of [Sound].
     */
    fun load(bytes: ByteArray): Sound

    /**
     * Load sound from asset like instance.
     *
     * @param asset Asset with bytes of sound.
     *
     * @return Instance of [Sound].
     */
    fun load(asset: AssetLike): Sound = load(asset.bytes)

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(format: SoundFormat): SoundLoader

    }

    companion object {

        /**
         * [SoundFormat.OGG] loader.
         */
        @JvmField
        val OGG = of(SoundFormat.OGG)

        /**
         * [SoundFormat.WAV] loader.
         */
        @JvmField
        val WAV = of(SoundFormat.WAV)

        /**
         * [SoundFormat.MP3] loader.
         */
        @JvmField
        val MP3 = of(SoundFormat.MP3)

        /**
         * [SoundFormat.FLAC] loader.
         */
        @JvmField
        val FLAC = of(SoundFormat.FLAC)

        /**
         * Get instance of loader for specific format.
         *
         * @param format Format for getting loader.
         *
         * @return Instance of [SoundLoader].
         */
        @JvmStatic
        fun of(format: SoundFormat): SoundLoader {
            return single<Factory>().create(format)
        }

    }

}