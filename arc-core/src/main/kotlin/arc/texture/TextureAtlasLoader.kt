package arc.texture

import arc.Arc
import arc.annotations.TypeFactory
import arc.asset.AssetLike
import arc.texture.BufferUtil.createDirectByteBuffer
import org.jetbrains.annotations.ApiStatus
import java.nio.ByteBuffer

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
    override fun load(buffer: ByteBuffer): TextureAtlas = load(buffer, -1, -1)

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
    override fun load(buffer: ByteBuffer, width: Int, height: Int): TextureAtlas

    /**
     * Load texture atlas via byte array.
     *
     * @param bytes Bytes of image.
     *
     * @return New instance of [Texture].
     */
    override fun load(bytes: ByteArray): TextureAtlas = load(bytes.createDirectByteBuffer(), -1, -1)

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
    override fun load(bytes: ByteArray, width: Int, height: Int): TextureAtlas = load(bytes.createDirectByteBuffer(), width, height)

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
    @ApiStatus.Internal
    interface Factory {

        fun create(format: TextureFormat): TextureAtlasLoader

    }

    companion object {

        /**
         * [TextureFormat.RAW] loader.
         */
        @JvmField
        val RAW = of(TextureFormat.RAW)

        /**
         * [TextureFormat.PNG] loader.
         */
        @JvmField
        val PNG = of(TextureFormat.PNG)

        /**
         * Get loader for some format.
         *
         * @param format Format of loader.
         *
         * @return Instance of [TextureAtlasLoader].
         */
        @JvmStatic
        fun of(format: TextureFormat): TextureAtlasLoader {
            return Arc.factory<Factory>().create(format)
        }

    }

}