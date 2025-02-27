package arc.texture

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.assets.TextureAsset
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

    /**
     * Width of this texture.
     */
    @get:JvmName("width")
    val width: Int

    /**
     * Height of this texture.
     */
    @get:JvmName("height")
    val height: Int

    /**
     * Binds the texture to the current rendering context.
     *
     * This method activates the texture, making it available for use in subsequent
     * rendering operations. It is typically called before rendering tasks that
     * involve this texture, such as drawing a mesh that uses the texture for
     * materials or effects.
     *
     * Once bound, the texture remains active until it is unbound or another
     * texture is bound in its place.
     */
    override fun bind()

    /**
     * Unbinds the texture from the current rendering context.
     *
     * This method deactivates the texture, indicating that it is no longer
     * used for subsequent rendering operations. It is typically called after
     * rendering tasks that utilize the texture are completed, or before
     * another texture is bound to ensure proper state management in the
     * rendering pipeline.
     */
    override fun unbind()

    /**
     * Releases the resources associated with the texture.
     *
     * This method is responsible for cleaning up any allocated resources tied to the texture,
     * ensuring that memory is freed and potential leaks are avoided. It should be called when
     * the texture is no longer needed, such as during application shutdown or when the texture
     * lifecycle has ended.
     */
    fun cleanup()

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
        fun create(asset: TextureAsset): Texture {
            return Arc.factory<Factory>().create(asset)
        }

    }

}