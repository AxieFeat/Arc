package arc.graphics

import arc.assets.shader.FragmentShader
import arc.assets.shader.ShaderData
import arc.assets.shader.VertexShader
import arc.shader.ShaderInstance
import arc.shader.UniformProvider
import java.io.File

object EmptyShaderInstance : ShaderInstance {

    override val id: Int = 0
    override val vertex: VertexShader = VertexShader.from(File(""))
    override val fragment: FragmentShader = FragmentShader.from(File(""))
    override val data: ShaderData = ShaderData.from(File(""))
    override fun configure(provider: UniformProvider) {

    }

    override fun compileShaders() {

    }

    override fun bind() {

    }

    override fun unbind() {

    }
}