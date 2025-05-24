package arc.texture

/**
 * Empty realization of [arc.texture.Texture].
 *
 * Can be used as a stub instead of the nullable type.
 */
object EmptyTexture : Texture {

    override val id: Int = -1

    override fun bind() {}

    override fun unbind() {}

    override fun cleanup() {}

}