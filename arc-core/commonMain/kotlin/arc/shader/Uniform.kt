package arc.shader

/**
 * This interface represents uniform consumer for shaders.
 */
fun interface Uniform {

    /**
     * Provide this uniform to shader instance.
     *
     * @param shader Shader instance to provide uniform to.
     * @param name Name of the uniform in the shader.
     */
    fun provide(shader: ShaderInstance, name: String)
}
