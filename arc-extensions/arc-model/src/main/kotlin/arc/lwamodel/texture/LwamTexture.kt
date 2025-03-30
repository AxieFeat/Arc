package arc.lwamodel.texture

import arc.Arc
import arc.annotations.TypeFactory
import arc.model.texture.ModelTexture
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents model texture in LWAM format.
 */
interface LwamTexture : ModelTexture {

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(
            id: Int,
            width: Int,
            height: Int,
            base64Image: String
        ): LwamTexture

    }

    companion object {

        /**
         * Create new instance of [LwamTexture].
         *
         * @param id ID of texture.
         * @param width Width of texture.
         * @param height Height of texture.
         * @param base64Image Base64 image in png format.
         *
         * @return New instance of [LwamTexture].
         */
        @JvmStatic
        fun of(
            id: Int,
            width: Int,
            height: Int,
            base64Image: String
        ): LwamTexture {
            return Arc.factory<Factory>().create(id, width, height, base64Image)
        }

    }

}