package arc.texture

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.asset.TextureAsset
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a texture in the rendering system.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Texture : TextureLike {

    /**
     * Asset of this texture. If null - texture available only in runtime.
     */
    @get:JvmName("asset")
    val asset: TextureAsset?

    @ApiStatus.Internal
    interface Factory {

        fun create(asset: TextureAsset): Texture

        fun create(bytes: ByteArray): Texture

    }

    companion object {

        /**
         * Create [Texture] from [TextureAsset].
         *
         * @param asset Asset for Texture.
         *
         * @return New instance of [Texture].
         */
        @JvmStatic
        fun from(asset: TextureAsset): Texture {
            return Arc.factory<Factory>().create(asset)
        }

        /**
         * Create [Texture] from byte array.
         *
         * @param bytes Bytes of texture.
         *
         * @return New instance of [Texture].
         */
        @JvmStatic
        fun from(bytes: ByteArray): Texture {
            return Arc.factory<Factory>().create(bytes)
        }

    }

}