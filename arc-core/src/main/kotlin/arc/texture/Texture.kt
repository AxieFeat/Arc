package arc.texture

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.asset.AssetLike
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a texture in the rendering system.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Texture : TextureLike {

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(bytes: ByteArray): Texture

    }

    companion object {

        /**
         * Create [Texture] from [AssetLike] object.
         *
         * @param asset Asset for Texture.
         *
         * @return New instance of [Texture].
         */
        @JvmStatic
        fun from(asset: AssetLike): Texture = from(asset.bytes)

        /**
         * Create [Texture] from [AssetLike] object.
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