package arc.texture

import arc.annotations.ImmutableType
import arc.asset.AssetLike
import java.nio.ByteBuffer

/**
 * This interface represents texture atlas.
 */
@ImmutableType
interface TextureAtlas : Texture {

    /**
     * Width of texture.
     */
    val width: Int

    /**
     * Height of texture.
     */
    val height: Int

    /**
     * Get UV coordinates for some position in this atlas.
     *
     * @param x X count of pixels.
     * @param y Y count of pixels.
     *
     * @return Pair of U and V coords.
     */
    fun uv(x: Int, y: Int): Pair<Float, Float>

    companion object {

        /**
         * Create [TextureAtlas] from [AssetLike] object.
         *
         * @param asset Asset for Texture Atlas in png format.
         *
         * @return New instance of [TextureAtlas].
         */
        @JvmStatic
        fun png(asset: AssetLike): TextureAtlas = TextureAtlasLoader.PNG.load(asset)

        /**
         * Create [TextureAtlas] from a byte array.
         *
         * @param bytes Bytes of texture in png format.
         *
         * @return New instance of [TextureAtlas].
         */
        @JvmStatic
        fun png(bytes: ByteArray): TextureAtlas = TextureAtlasLoader.PNG.load(bytes)

        /**
         * Create [TextureAtlas] from byte buffer.
         *
         * @param buffer Byte buffer of texture in png format.
         *
         * @return New instance of [TextureAtlas].
         */
        @JvmStatic
        fun png(buffer: ByteBuffer): TextureAtlas = TextureAtlasLoader.PNG.load(buffer)

        /**
         * Create [TextureAtlas] from [AssetLike] object.
         *
         * @param asset Asset for Texture Atlas in raw format.
         *
         * @return New instance of [TextureAtlas].
         */
        @JvmStatic
        fun raw(asset: AssetLike, width: Int, height: Int): TextureAtlas = TextureAtlasLoader.RAW.load(asset, width, height)

        /**
         * Create [TextureAtlas] from a byte array.
         *
         * @param bytes Bytes of texture in raw format.
         *
         * @return New instance of [TextureAtlas].
         */
        @JvmStatic
        fun raw(bytes: ByteArray, width: Int, height: Int): TextureAtlas = TextureAtlasLoader.RAW.load(bytes, width, height)

        /**
         * Create [TextureAtlas] from byte buffer.
         *
         * @param buffer Byte buffer of texture in raw format.
         *
         * @return New instance of [TextureAtlas].
         */
        @JvmStatic
        fun raw(buffer: ByteBuffer, width: Int, height: Int): TextureAtlas = TextureAtlasLoader.RAW.load(buffer, width, height)

    }

}