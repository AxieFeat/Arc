package arc.model.texture

internal data class ArcModelTexture(
    override val id: Int = 0,
    override val width: Int = 0,
    override val height: Int = 0,
    override val base64Image: String = ""
) : ModelTexture {

    object Factory : ModelTexture.Factory {
        override fun create(
            id: Int,
            width: Int,
            height: Int,
            base64Image: String
        ): ModelTexture {
            return ArcModelTexture(id, width, height, base64Image)
        }

    }

}