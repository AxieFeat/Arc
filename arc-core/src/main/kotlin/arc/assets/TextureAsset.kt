package arc.assets

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.graphics.Texture
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * Represents a texture asset in the system.
 *
 * Texture assets are immutable and provide functionality to interact with
 * textures in the rendering pipeline. This includes the ability to retrieve
 * the texture object, bind and unbind the asset within the render frame, and
 * manage its lifecycle.
 *
 * This interface extends the [Asset] interface, inheriting the properties of
 * a general asset, such as its association with a file.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface TextureAsset : Asset {

    /**
     * Provides access to the underlying texture associated with this asset.
     *
     * This property returns an immutable instance of [Texture], representing
     * the texture object that can be used in rendering operations. The returned
     * texture encapsulates image data and metadata such as its dimensions.
     */
    @get:JvmName("texture")
    val texture: Texture

    /**
     * Binds the texture asset to the current rendering context.
     *
     * This method ensures that the associated texture is activated and available for
     * rendering operations. It is typically invoked before performing tasks
     * that involve this texture, such as rendering a mesh that uses the texture
     * for material application or effects.
     *
     * Once bound, the texture remains active until it is explicitly unbound or
     * another texture is bound in its place.
     *
     * @return The bound [Texture] instance associated with this texture asset.
     */
    fun bind(): Texture

    /**
     * Unbinds the texture asset from the current rendering context.
     *
     * This method deactivates the associated texture, ensuring it is no longer
     * used in subsequent rendering operations. It is typically invoked after
     * completing tasks that involve the texture or before switching to another
     * texture in the rendering context.
     *
     * After this method is called, the texture must be explicitly bound again
     * using the [bind] method if it is needed for further operations.
     */
    fun unbind()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create instance of [TextureAsset] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [TextureAsset].
         */
        fun create(file: File): TextureAsset

    }

    companion object {

        /**
         * Create instance of [TextureAsset] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [TextureAsset].
         */
        @JvmStatic
        fun from(file: File): TextureAsset {
            return Arc.factory<Factory>().create(file)
        }

    }

}