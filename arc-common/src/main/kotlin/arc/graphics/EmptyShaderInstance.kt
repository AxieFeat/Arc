package arc.graphics

import arc.assets.shader.FragmentShader
import arc.assets.shader.VertexShader
import arc.shader.ShaderInstance
import java.io.File

object EmptyShaderInstance : ShaderInstance {

    override val vertex: VertexShader = VertexShader.from(File(""))
    override val fragment: FragmentShader = FragmentShader.from(File(""))

    override fun compileShaders() {

    }

    override fun bind() {

    }

    override fun unbind() {

    }
}