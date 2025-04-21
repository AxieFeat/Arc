package arc.model.texture

import arc.Arc
import arc.annotations.ImmutableType
import arc.texture.Texture
import arc.texture.TextureAtlas
import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * Represents a texture associated with a 3D model.
 */
@ImmutableType
interface ModelTexture {

    /**
     * Serialized base64 image in png format.
     */
    val base64Image: String

    /**
     * Create texture atlas by [base64Image] serialized image.
     *
     * @return New instance of [TextureAtlas].
     */
    fun toAtlasTexture(): TextureAtlas {
        return TextureAtlas.png(Base64.getDecoder().decode(base64Image))
    }

    @ApiStatus.Internal
    interface Builder : arc.util.pattern.Builder<ModelTexture> {

        fun setImage(base64Image: String): Builder

    }

    companion object {

        /**
         * Create new instance of [Texture] via builder.
         *
         * @return New instance of [Builder].
         */
        @JvmStatic
        fun builder(): Builder {
            return Arc.factory<Builder>()
        }

    }

}