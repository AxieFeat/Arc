package arc.texture

/**
 * This interface represents general texture.
 */
interface TextureLike {

    /**
     * Binds the texture to the current rendering frame.
     */
    fun bind()

    /**
     * Unbinds the texture from the current rendering frame.
     */
    fun unbind()

}