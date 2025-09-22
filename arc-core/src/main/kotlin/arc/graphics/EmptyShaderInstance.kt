package arc.graphics

import arc.shader.ShaderInstance
import arc.shader.ShaderSettings
import arc.shader.UniformBuffer
import arc.shader.UniformProvider
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f

/**
 * Empty realization of [ShaderInstance].
 *
 * Can be used as a stub instead of the nullable type.
 */
@Suppress("EmptyFunctionBlock") // Because it's an empty implementation.
object EmptyShaderInstance : ShaderInstance {

    override val id: Int = -1
    override val vertex: String = ""
    override val fragment: String = ""

    override val settings: ShaderSettings = ShaderSettings.of()

    override fun addProvider(provider: UniformProvider) {}

    override fun setUniform(name: String, value: Boolean) {}
    override fun setUniform(name: String, value: Int) {}
    override fun setUniform(name: String, value: Float) {}
    override fun setUniform(name: String, value: Matrix3f) {}
    override fun setUniform(name: String, value: Matrix4f) {}
    override fun setUniform(name: String, value: Vector4f) {}
    override fun setUniform(name: String, value: Vector3f) {}
    override fun setUniform(name: String, value: Vector2f) {}
    override fun uploadUniformBuffer(name: String, buffer: UniformBuffer) {}

    override fun compileShaders() {}

    override fun bind() {}
    override fun unbind() {}
    override fun cleanup() {}
}
