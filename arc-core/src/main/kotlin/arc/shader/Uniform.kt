package arc.shader

/**
 * This interface represents uniform consumer for shaders.
 */
fun interface Uniform {

    /**
     * Provide this uniform to shader instance.
     */
    fun provide(shader: ShaderInstance)

}