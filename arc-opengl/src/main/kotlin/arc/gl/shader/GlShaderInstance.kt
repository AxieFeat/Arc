package arc.gl.shader

import arc.asset.shader.FragmentShader
import arc.asset.shader.ShaderData
import arc.asset.shader.VertexShader
import arc.gl.graphics.GlRenderSystem
import arc.graphics.EmptyShaderInstance
import arc.shader.ShaderInstance
import arc.shader.UniformProvider
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glGetUniformLocation
import org.lwjgl.opengl.GL41
import org.lwjgl.system.MemoryStack

internal data class GlShaderInstance(
    override val vertex: VertexShader,
    override val fragment: FragmentShader,
    override val data: ShaderData
) : ShaderInstance {

    override var id = 0
    private var vertexShaderId = 0
    private var fragmentShaderId = 0

    private val providers = mutableListOf<UniformProvider>()

    override fun compileShaders() {
        id = GL41.glCreateProgram()
        vertexShaderId = createShader(vertex.file.readText(), GL41.GL_VERTEX_SHADER)
        fragmentShaderId = createShader(fragment.file.readText(), GL41.GL_FRAGMENT_SHADER)

        link()
    }

    override fun bind() {
        GL41.glUseProgram(id)
        GlRenderSystem.shader = this

        providers.forEach { it.provide(this) }
    }

    override fun unbind() {
        GL41.glUseProgram(0)

        GlRenderSystem.shader = EmptyShaderInstance
    }

    override fun addProvider(provider: UniformProvider) {
        providers.add(provider)
    }

    override fun setUniform(name: String, value: Int) {
        uniform(name) { glUniform1i(this, value) }
    }

    override fun setUniform(name: String, value: Float) {
        uniform(name) { glUniform1f(this, value) }
    }

    override fun setUniform(name: String, value: Matrix4f) {
        uniform(name) {
            MemoryStack.stackPush().use { stack ->
                glUniformMatrix4fv(
                    this,
                    false,
                    value[stack.mallocFloat(16)]
                )
            }
        }
    }

    override fun setUniform(name: String, value: Vector4f) {
        uniform(name) {
            glUniform4f(this, value.x, value.y, value.z, value.w)
        }
    }

    override fun setUniform(name: String, value: Vector3f) {
        uniform(name) {
            glUniform3f(this, value.x, value.y, value.z)
        }
    }

    override fun setUniform(name: String, value: Vector2f) {
        uniform(name) {
            glUniform2f(this, value.x, value.y)
        }
    }

    private fun uniform(name: String, value: Int.() -> Unit) {
        val location: Int = glGetUniformLocation(id, name)

        if(location != -1) {
            value.invoke(location)
        }
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
            shaderData: ShaderData
        ): ShaderInstance {
            return GlShaderInstance(vertexShader, fragmentShader, shaderData)
        }
    }

}