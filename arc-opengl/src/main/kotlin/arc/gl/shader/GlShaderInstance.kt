package arc.gl.shader

import arc.assets.shader.FragmentShader
import arc.assets.shader.VertexShader
import arc.shader.ShaderInstance
import arc.shader.ShaderUniforms
import org.lwjgl.opengl.GL41

internal data class GlShaderInstance(
    override val vertex: VertexShader,
    override val fragment: FragmentShader,
    override val uniforms: ShaderUniforms
) : ShaderInstance {

    override var id = 0
    private var vertexShaderId = 0
    private var fragmentShaderId = 0

    override fun compileShaders() {
        id = GL41.glCreateProgram()
        vertexShaderId = createShader(vertex.file.readText(), GL41.GL_VERTEX_SHADER)
        fragmentShaderId = createShader(fragment.file.readText(), GL41.GL_FRAGMENT_SHADER)

        link()
    }

    override fun bind() {
        GL41.glUseProgram(id)
    }

    override fun unbind() {
        GL41.glUseProgram(0)
    }

    private fun createShader(shaderCode: String, shaderType: Int): Int {
        val shaderId = GL41.glCreateShader(shaderType)
        if (shaderId == 0) {
            throw RuntimeException("Error creating shader. Type: $shaderType")
        }

        GL41.glShaderSource(shaderId, shaderCode)
        GL41.glCompileShader(shaderId)

        if (GL41.glGetShaderi(shaderId, GL41.GL_COMPILE_STATUS) == 0) {
            throw RuntimeException("Error compiling Shader code: " + GL41.glGetShaderInfoLog(shaderId, 1024))
        }

        GL41.glAttachShader(id, shaderId)

        return shaderId
    }

    private fun link() {
        GL41.glLinkProgram(id)

        check(GL41.glGetProgrami(id, GL41.GL_LINK_STATUS) != 0) {
            "Error linking Shader code: ${GL41.glGetProgramInfoLog(id, 1024)}"
        }

        GL41.glDetachShader(
            id,
            vertexShaderId
        )
        GL41.glDetachShader(
            id,
            fragmentShaderId
        )


        GL41.glDeleteShader(
            vertexShaderId
        )
        GL41.glDeleteShader(
            fragmentShaderId
        )
    }


    object Factory : ShaderInstance.Factory {
        override fun create(
            vertexShader: VertexShader,
            fragmentShader: FragmentShader,
            uniforms: ShaderUniforms
        ): ShaderInstance {
            return GlShaderInstance(vertexShader, fragmentShader, uniforms)
        }
    }

}