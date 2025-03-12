package arc.texture

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.assets.TextureAsset
import arc.math.Point2i
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents texture atlas.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface TextureAtlas : TextureLike {

    /**
     * Center of this texture.
     */
    @get:JvmName("origin")
    val origin: Point2i

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
     * Min U coordinate in this atlas.
     */
    @get:JvmName("minU")
    val minU: Int

    /**
     * Max U coordinate in this atlas.
     */
    @get:JvmName("maxU")
    val maxU: Int

    /**
     * Min V coordinate in this atlas.
     */
    @get:JvmName("minV")
    val minV: Int

    /**
     * Max V coordinate in this atlas.
     */
    @get:JvmName("maxV")
    val maxV: Int

    /**
     * Asset of this atlas.
     */
    @get:JvmName("asset")
    val asset: TextureAsset

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create [TextureAtlas] from [TextureAsset].
         *
         * @param asset Asset for Texture Atlas.
         *
         * @return New instance of [TextureAtlas].
         */
        fun create(asset: TextureAsset): TextureAtlas

    }

    companion object {

        /**
         * Create [TextureAtlas] from [TextureAsset].
         *
         * @param asset Asset for Texture Atlas.
         *
         * @return New instance of [TextureAtlas].
         */
        @JvmStatic
        fun create(asset: TextureAsset): TextureAtlas {
            return Arc.factory<Factory>().create(asset)
        }

    }

}