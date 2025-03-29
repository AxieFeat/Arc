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
     * Asset of this texture.
     */
    @get:JvmName("asset")
    val asset: TextureAsset

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

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