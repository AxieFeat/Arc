package arc.shader

/**
 * This class represents basic [UniformProvider]. Use it for creation own providers.
 */
abstract class AbstractUniformProvider : UniformProvider {

    override val uniforms: MutableMap<String, Uniform> = mutableMapOf()

    override fun addUniform(name: String, uniform: Uniform) {
        uniforms[name] = uniform
    }

    override fun provide(shader: ShaderInstance) {
        uniforms.filter { shader.settings.uniforms.contains(it.key) }.forEach {
            it.value.provide(shader, it.key)
        }
    }
}
