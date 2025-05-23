package arc.texture

object EmptyTexture : Texture {

    override val id: Int = -1

    override fun bind() {}

    override fun unbind() {}

    override fun cleanup() {}

}