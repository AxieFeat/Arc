package arc.texture

import kotlin.jvm.JvmStatic

import arc.Arc.single
import arc.annotations.TypeFactory
import arc.asset.AssetLike
import arc.texture.BufferUtil.createDirectBuffer
import arc.annotations.InternalApi
import com.ditchoom.buffer.PlatformBuffer

/**
 * This interface represents loader for texture atlas.
 */
interface TextureAtlasLoader : TextureLoader {

    /**
     * Load texture atlas via buffer.
     *
     * @param buffer Buffer of image.
     *
     * @return New instance of [Texture].
     */
    override fun load(buffer: PlatformBuffer): TextureAtlas = load(buffer, -1, -1)

    /**
     * Load texture atlas via buffer.
     *
     * Note: [width] and [height] required only for [TextureFormat.RAW].
     *
     * @param buffer Buffer of image.
     * @param width Width of image.
     * @param height Height of image.
     *
     * @return New instance of [Texture].
     */
    override fun load(buffer: PlatformBuffer, width: Int, height: Int): TextureAtlas

    /**
     * Load texture atlas via byte array.
     *
     * @param bytes Bytes of image.
     *
     * @return New instance of [Texture].
     */
    override fun load(bytes: ByteArray): TextureAtlas = load(bytes.createDirectBuffer(), -1, -1)

    /**
     * Load texture atlas via byte array.
     *
     * Note: [width] and [height] required only for [TextureFormat.RAW].
     *
     * @param bytes Bytes of image.
     * @param width Width of image.
     * @param height Height of image.
     *
     * @return New instance of [Texture].
     */
    override fun load(bytes: ByteArray, width: Int, height: Int): TextureAtlas = load(bytes.createDirectBuffer(), width, height)

    /**
     * Load texture atlas from [AssetLike] object.
     *
     * @param asset Asset with bytes of image.
     *
     * @return New instance of [Texture].
     */
    override fun load(asset: AssetLike): TextureAtlas = load(asset.bytes, -1, -1)

    /**
     * Load texture atlas from [AssetLike] object.
     *
     * Note: [width] and [height] required only for [TextureFormat.RAW].
     *
     * @param asset Asset with bytes of image.
     * @param width Width of image.
     * @param height Height of image.
     *
     * @return New instance of [Texture].
     */
    override fun load(asset: AssetLike, width: Int, height: Int): TextureAtlas = load(asset.bytes, width, height)

    @TypeFactory
    @InternalApi
    interface Factory {

        fun create(format: TextureFormat): TextureAtlasLoader
    }

    companion object {

        /**
         * [TextureFormat.RAW] loader.
         */
        val RAW by lazy { of(TextureFormat.RAW) }

        /**
         * [TextureFormat.PNG] loader.
         */
        val PNG by lazy { of(TextureFormat.PNG) }

        /**
         * Get loader for some format.
         *
         * @param format Format of loader.
         *
         * @return Instance of [TextureAtlasLoader].
         */
        @JvmStatic
        fun of(format: TextureFormat): TextureAtlasLoader {
            return single<Factory>().create(format)
        }
    }
}
