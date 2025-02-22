package arc.graphics.g3d.model.texture

import arc.annotations.ImmutableType
import arc.graphics.Texture
import arc.graphics.g3d.model.Model

/**
 * Represents a texture associated with a 3D model.
 *
 * This interface provides metadata and rendering properties for textures
 * used in 3D models, such as dimensions and identifiers. It is an immutable type,
 * ensuring the state of the texture cannot be modified after creation.
 * The `ModelTexture` is typically used in conjunction with 3D model elements.
 *
 * Properties of `ModelTexture` enable efficient texture management and rendering,
 * including mapping and scaling operations within the 3D graphics pipeline.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface ModelTexture {

    /**
     * Unique identifier for the texture.
     *
     * This property represents an immutable integer value that uniquely identifies the texture
     * within the rendering system or asset management pipeline. It is used for referencing and
     * managing textures efficiently, ensuring that each texture is distinctly recognized.
     */
    @get:JvmName("id")
    val id: Int

    /**
     * Width of this texture in pixels.
     *
     * This property represents the horizontal dimension of the texture, defining the number of pixels
     * along its width. It provides essential metadata for rendering operations and texture management,
     * including mapping and scaling in 3D graphics pipelines. The width is immutable and intrinsic to
     * the texture's data.
     */
    @get:JvmName("width")
    val width: Int

    /**
     * Height of this texture in pixels.
     *
     * Represents the vertical dimension of the texture, defining the number of pixels
     * along its height. This property is immutable, providing essential metadata for
     * rendering operations and texture management, particularly in 3D graphics pipelines.
     */
    @get:JvmName("height")
    val height: Int

    /**
     * The width of the UV coordinates for a texture.
     *
     * This property represents the horizontal extent of the UV mapping
     * applied to a texture. It is used to define the portion of the texture
     * that is covered along the width axis when rendering. The value is in pixels
     * and corresponds to the width of the UV-mapped area.
     */
    @get:JvmName("uvWidth")
    val uvWidth: Int

    /**
     * Represents the height of the UV mapping region in a texture.
     *
     * UV mapping is a process that assigns 2D texture coordinates (U and V) to a 3D model.
     * This property specifies the height of the area covered by the UV mapping in the texture,
     * measured in pixels. It is typically used in rendering or texture atlas workflows to
     * define the region of a texture that is applied to a model.
     */
    @get:JvmName("uvHeight")
    val uvHeight: Int

    /**
     * Represents the texture associated with this model.
     *
     * This property provides access to the immutable [Texture] object
     * utilized in the rendering system. The texture contains the image data
     * and metadata required to properly render the visual representation of
     * the model, such as dimensions and asset binding details.
     *
     * The encapsulated texture can be used for rendering tasks, such as
     * binding the texture within a rendering context or releasing its
     * resources once it's no longer needed.
     *
     * This property is annotated with `@JvmName` to provide interoperability
     * with Java, ensuring that the getter method is available under the
     * specified name.
     */
    @get:JvmName("texture")
    val texture: Texture

}