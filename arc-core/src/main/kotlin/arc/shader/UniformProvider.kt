package arc.shader

import arc.annotations.MutableType
import arc.assets.shader.ShaderData

/**
 * This interface represents provider for shader uniforms.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface UniformProvider {

    /**
     * All uniforms in this provider.
     */
    @get:JvmName("uniforms")
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
     * For providing used information for [ShaderData.uniforms].
     */
    fun provide(shader: ShaderInstance)

}