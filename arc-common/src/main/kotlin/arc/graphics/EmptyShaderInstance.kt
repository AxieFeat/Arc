package arc.graphics

import arc.asset.shader.FragmentShader
import arc.asset.shader.ShaderData
import arc.asset.shader.VertexShader
import arc.shader.ShaderInstance
import arc.shader.UniformProvider
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import java.io.File

object EmptyShaderInstance : ShaderInstance {

    override val id: Int = 0
    override val vertex: VertexShader = VertexShader.from(File(""))
    override val fragment: FragmentShader = FragmentShader.from(File(""))
    override val data: ShaderData = ShaderData.from(File(""))

    override fun addProvider(provider: UniformProvider) {}

    override fun setUniform(name: String, value: Int) {}
    override fun setUniform(name: String, value: Float) {}
    override fun setUniform(name: String, value: Matrix4f) {}
    override fun setUniform(name: String, value: Vector4f) {}
    override fun setUniform(name: String, value: Vector3f) {}
    override fun setUniform(name: String, value: Vector2f) {}

    override fun compileShaders() {}

    override fun bind() {}
    override fun unbind() {}
}