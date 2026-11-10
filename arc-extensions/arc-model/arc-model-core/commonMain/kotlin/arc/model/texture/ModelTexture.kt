package arc.model.texture

import kotlin.jvm.JvmStatic
import arc.Arc.factory
import arc.annotations.ImmutableType
import arc.texture.Texture
import arc.texture.TextureAtlas
import arc.annotations.InternalApi
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

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
    @OptIn(ExperimentalEncodingApi::class)
    fun toAtlasTexture(): TextureAtlas {
        return TextureAtlas.png(Base64.Default.decode(base64Image))
    }

    @InternalApi
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
            return factory<Builder>()
        }
    }
}
