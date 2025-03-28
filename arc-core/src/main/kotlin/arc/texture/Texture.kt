package arc.texture

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.asset.TextureAsset
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a texture in the rendering system.
 *
 * A texture is an immutable object that encapsulates image data,
 * along with its associated metadata such as dimensions. This interface
 * provides methods for binding and unbinding the texture during rendering
 * and for cleaning up its resources.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Texture : TextureLike {

    /**
     * Asset of this texture.
     */
    @get:JvmName("asset")
    val asset: TextureAsset

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create [Texture] from [TextureAsset].
         *
         * @param asset Asset for Texture.
         *
         * @return New instance of [Texture].
         */
        fun create(asset: TextureAsset): Texture

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

    }

}