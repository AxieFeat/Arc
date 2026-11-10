package arc.graphics

import arc.shader.ShaderInstance
import arc.shader.ShaderSettings
import arc.shader.UniformBuffer
import arc.shader.UniformProvider
import korlibs.math.geom.Matrix3
import korlibs.math.geom.Matrix4
import korlibs.math.geom.Vector2F
import korlibs.math.geom.Vector3F
import korlibs.math.geom.Vector4F

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
    override fun setUniform(name: String, value: Matrix3) {}
    override fun setUniform(name: String, value: Matrix4) {}
    override fun setUniform(name: String, value: Vector4F) {}
    override fun setUniform(name: String, value: Vector3F) {}
    override fun setUniform(name: String, value: Vector2F) {}
    override fun uploadUniformBuffer(name: String, buffer: UniformBuffer) {}

    override fun compileShaders() {}

    override fun bind() {}
    override fun unbind() {}
    override fun cleanup() {}
}
