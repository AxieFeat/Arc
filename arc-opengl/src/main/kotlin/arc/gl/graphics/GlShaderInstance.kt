package arc.gl.graphics

import arc.assets.shader.FragmentShader
import arc.assets.shader.VertexShader
import arc.shader.ShaderInstance
import org.lwjgl.opengl.GL41

internal data class GlShaderInstance(
    override val vertex: VertexShader,
    override val fragment: FragmentShader
) : ShaderInstance {

    private var programId = 0
    private var vertexShaderId = 0
    private var fragmentShaderId = 0

    override fun compileShaders() {
        programId = GL41.glCreateProgram()
        vertexShaderId = createShader(vertex.file.readText(), GL41.GL_VERTEX_SHADER)
        fragmentShaderId = createShader(fragment.file.readText(), GL41.GL_FRAGMENT_SHADER)

        link()
    }

    override fun bind() {
        GL41.glUseProgram(programId)
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

        GL41.glAttachShader(programId, shaderId)

        return shaderId
    }

    private fun link() {
        GL41.glLinkProgram(programId)

        check(GL41.glGetProgrami(programId, GL41.GL_LINK_STATUS) != 0) {
            "Error linking Shader code: ${GL41.glGetProgramInfoLog(programId, 1024)}"
        }

        GL41.glDetachShader(
            programId,
            vertexShaderId
        )
        GL41.glDetachShader(
            programId,
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
        override fun create(vertexShader: VertexShader, fragmentShader: FragmentShader): ShaderInstance {
            return GlShaderInstance(vertexShader, fragmentShader)
        }
    }

}