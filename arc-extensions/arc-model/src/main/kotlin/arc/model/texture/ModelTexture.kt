package arc.model.texture

import arc.annotations.ImmutableType

/**
 * Represents a texture associated with a 3D model.
 */
@ImmutableType
interface ModelTexture {

    /**
     * ID of this texture in Model. NOT in render system!
     */
    val id: Int

    /**
     * Width of [base64Image] image.
     */
    val width: Int

    /**
     * Height of [base64Image] image.
     */
    val height: Int

    /**
     * Serialized base64 image in png format.
     */
    val base64Image: String

}