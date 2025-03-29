package arc.model.texture

import arc.annotations.ImmutableType

/**
 * Represents a texture associated with a 3D model.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface ModelTexture {

    /**
     * ID of this texture in Model. NOT in render system!
     */
    @get:JvmName("id")
    val id: Int

    /**
     * Width of [base64Image] image.
     */
    @get:JvmName("width")
    val width: Int

    /**
     * Height of [base64Image] image.
     */
    @get:JvmName("height")
    val height: Int

    /**
     * Number of rows for atlas.
     */
    @get:JvmName("rows")
    val rows: Int

    /**
     * Number of columns for atlas.
     */
    @get:JvmName("columns")
    val columns: Int

    /**
     * Serialized base64 image in png format.
     */
    @get:JvmName("base64Image")
    val base64Image: String

}