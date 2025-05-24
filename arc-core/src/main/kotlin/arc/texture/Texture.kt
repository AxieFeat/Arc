package arc.texture

import arc.annotations.ImmutableType
import arc.asset.AssetLike
import java.nio.ByteBuffer

/**
 * Represents a texture in the rendering system.
 */
@ImmutableType
interface Texture : TextureLike {

    companion object {

        /**
         * Create [Texture] from [AssetLike] object.
         *
         * @param asset Asset for Texture in png format.
         *
         * @return New instance of [Texture].
         */
        @JvmStatic
        fun png(asset: AssetLike): Texture = TextureLoader.PNG.load(asset)

        /**
         * Create [Texture] from a byte array.
         *
         * @param bytes Bytes of texture in png format.
         *
         * @return New instance of [Texture].
         */
        @JvmStatic
        fun png(bytes: ByteArray): Texture = TextureLoader.PNG.load(bytes)

        /**
         * Create [Texture] from byte buffer.
         *
         * @param buffer Byte buffer of texture in png format.
         *
         * @return New instance of [Texture].
         */
        @JvmStatic
        fun png(buffer: ByteBuffer): Texture = TextureLoader.PNG.load(buffer)

        /**
         * Create [Texture] from [AssetLike] object.
         *
         * @param asset Asset for Texture in raw format.
         *
         * @return New instance of [Texture].
         */
        @JvmStatic
        fun raw(asset: AssetLike): Texture = TextureLoader.RAW.load(asset)

        /**
         * Create [Texture] from a byte array.
         *
         * @param bytes Bytes of texture in raw format.
         *
         * @return New instance of [Texture].
         */
        @JvmStatic
        fun raw(bytes: ByteArray): Texture = TextureLoader.RAW.load(bytes)

        /**
         * Create [Texture] from byte buffer.
         *
         * @param buffer Byte buffer of texture in raw format.
         *
         * @return New instance of [Texture].
         */
        @JvmStatic
        fun raw(buffer: ByteBuffer): Texture = TextureLoader.RAW.load(buffer)

    }

}