package arc.lwamodel.texture

import kotlinx.serialization.Serializable

@Serializable
internal data class ArcLwamTexture(
    override val id: Int = 0,
    override val width: Int = 0,
    override val height: Int = 0,
    override val base64Image: String = ""
) : LwamTexture {

    object Factory : LwamTexture.Factory {
        override fun create(
            id: Int,
            width: Int,
            height: Int,
            base64Image: String
        ): LwamTexture {
            return ArcLwamTexture(id, width, height, base64Image)
        }

    }

}