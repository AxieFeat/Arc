package arc.lwamodel.texture

import kotlinx.serialization.Serializable

@Serializable
internal data class ArcLWAModelTexture(
    override val id: Int = 0,
    override val width: Int = 0,
    override val height: Int = 0,
    override val base64Image: String = ""
) : LWAModelTexture {

    object Factory : LWAModelTexture.Factory {
        override fun create(
            id: Int,
            width: Int,
            height: Int,
            base64Image: String
        ): LWAModelTexture {
            return ArcLWAModelTexture(id, width, height, base64Image)
        }

    }

}