package arc.shader

import arc.annotations.MutableType

/**
 * This interface represents provider for shader uniforms.
 *
 * @sample arc.sample.UniformProviderSample
 * @sample arc.sample.uniformProviderSample
 */
@MutableType
interface UniformProvider {

    /**
     * All uniforms in this provider.
     */
    val uniforms: Map<String, Uniform>

    /**
     * Add uniform to this provider.
     *
     * @param name Name of uniform.
     * @param uniform Just uniform.
     */
    fun addUniform(name: String, uniform: Uniform)

    /**
     * Provide required uniforms in this shader.
     *
     * For providing used information for [ShaderSettings.uniforms].
     */
    fun provide(shader: ShaderInstance)

}