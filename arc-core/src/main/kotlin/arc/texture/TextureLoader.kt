package arc.texture

import arc.Arc.single
import arc.annotations.TypeFactory
import arc.asset.AssetLike
import arc.texture.BufferUtil.createDirectByteBuffer
import org.jetbrains.annotations.ApiStatus
import java.nio.ByteBuffer

/**
 * This interface represents a loader for textures in a specific format.
 */
interface TextureLoader {

    /**
     * Format of loader.
     */
    val format: TextureFormat

    /**
     * Load texture via buffer.
     *
     * @param buffer Buffer of image.
     *
     * @return New instance of [Texture].
     */
    fun load(buffer: ByteBuffer): Texture = load(buffer, -1, -1)

    /**
     * Load texture via buffer.
     *
     * Note: [width] and [height] required only for [TextureFormat.RAW].
     *
     * @param buffer Buffer of image.
     * @param width Width of image.
     * @param height Height of image.
     *
     * @return New instance of [Texture].
     */
    fun load(buffer: ByteBuffer, width: Int = -1, height: Int = -1): Texture

    /**
     * Load texture via byte array.
     *
     * @param bytes Bytes of image.
     *
     * @return New instance of [Texture].
     */
    fun load(bytes: ByteArray): Texture = load(bytes.createDirectByteBuffer())

    /**
     * Load texture via a byte array.
     *
     * Note: [width] and [height] required only for [TextureFormat.RAW].
     *
     * @param bytes Bytes of image.
     * @param width Width of image.
     * @param height Height of image.
     *
     * @return New instance of [Texture].
     */
    fun load(bytes: ByteArray, width: Int = -1, height: Int = -1): Texture = load(bytes.createDirectByteBuffer(), width, height)

    /**
     * Load texture from [AssetLike] object.
     *
     * @param asset Asset with bytes of image.
     *
     * @return New instance of [Texture].
     */
    fun load(asset: AssetLike): Texture = load(asset.bytes)

    /**
     * Load texture from [AssetLike] object.
     *
     * Note: [width] and [height] required only for [TextureFormat.RAW].
     *
     * @param asset Asset with bytes of image.
     * @param width Width of image.
     * @param height Height of image.
     *
     * @return New instance of [Texture].
     */
    fun load(asset: AssetLike, width: Int = -1, height: Int = -1): Texture = load(asset.bytes, width, height)

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(format: TextureFormat): TextureLoader

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
         * @return Instance of [TextureLoader].
         */
        @JvmStatic
        fun of(format: TextureFormat): TextureLoader {
            return single<Factory>().create(format)
        }

    }


}