package arc.graphics

import arc.assets.shader.FragmentShader
import arc.assets.shader.UniformAsset
import arc.assets.shader.VertexShader
import arc.shader.ShaderInstance
import arc.shader.ShaderUniforms
import java.io.File

object EmptyShaderInstance : ShaderInstance {

    override val id: Int = 0
    override val vertex: VertexShader = VertexShader.from(File(""))
    override val fragment: FragmentShader = FragmentShader.from(File(""))
    override val uniforms: ShaderUniforms = ShaderUniforms.from(UniformAsset.from(File("")))

    override fun compileShaders() {

    }

    override fun bind() {

    }

    override fun unbind() {

    }
}