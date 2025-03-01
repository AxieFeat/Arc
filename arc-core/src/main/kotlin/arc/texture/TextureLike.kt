package arc.texture

/**
 * This interface represents general texture.
 */
interface TextureLike {

    /**
     * ID of this texture in render system.
     */
    val id: Int

    /**
     * Binds the texture to the current rendering frame.
     */
    fun bind()

    /**
     * Unbinds the texture from the current rendering frame.
     */
    fun unbind()

}