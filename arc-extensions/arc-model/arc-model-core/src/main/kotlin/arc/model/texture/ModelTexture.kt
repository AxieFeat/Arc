package arc.model.texture

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.texture.Texture
import org.jetbrains.annotations.ApiStatus

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

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(
            id: Int,
            width: Int,
            height: Int,
            base64Image: String
        ): ModelTexture

    }

    companion object {

        /**
         * Create new instance of [Texture].
         *
         * @param id ID of texture.
         * @param width Width of texture.
         * @param height Height of texture.
         * @param base64Image Base64 image in png format.
         *
         * @return New instance of [Texture].
         */
        @JvmStatic
        fun of(
            id: Int,
            width: Int,
            height: Int,
            base64Image: String
        ): ModelTexture {
            return Arc.factory<Factory>().create(id, width, height, base64Image)
        }

    }

}