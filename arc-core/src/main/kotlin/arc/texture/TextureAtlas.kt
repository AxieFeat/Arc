package arc.texture

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.asset.AssetLike
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents texture atlas.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface TextureAtlas : TextureLike {

    /**
     * Width of texture.
     */
    @get:JvmName("width")
    val width: Int

    /**
     * Height of texture.
     */
    @get:JvmName("height")
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

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(bytes: ByteArray): TextureAtlas

    }

    companion object {

        /**
         * Create [TextureAtlas] from [AssetLike] object.
         *
         * @param asset Asset for Texture Atlas.
         *
         * @return New instance of [TextureAtlas].
         */
        @JvmStatic
        fun from(asset: AssetLike): TextureAtlas = from(asset.bytes)

        /**
         * Create [TextureAtlas] from [AssetLike] object.
         *
         * @param bytes Bytes of texture.
         *
         * @return New instance of [TextureAtlas].
         */
        @JvmStatic
        fun from(bytes: ByteArray): TextureAtlas {
            return Arc.factory<Factory>().create(bytes)
        }

    }

}