package arc.texture

import arc.annotations.ImmutableType

/**
 * This interface represents general texture.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface TextureLike {

    /**
     * ID of this texture in render system.
     */
    @get:JvmName("id")
    val id: Int

    /**
     * Binds the texture to the current rendering frame.
     */
    fun bind()

    /**
     * Unbinds the texture from the current rendering frame.
     */
    fun unbind()

    /**
     * Clean resources of this texture.
     */
    fun cleanup()

}