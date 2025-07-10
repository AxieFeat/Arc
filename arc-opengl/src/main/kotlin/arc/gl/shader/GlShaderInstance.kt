package arc.gl.shader

import arc.asset.StringAsset
import arc.gl.graphics.GlRenderSystem
import arc.graphics.EmptyShaderInstance
import arc.shader.ShaderInstance
import arc.shader.ShaderSettings
import arc.shader.UniformBuffer
import arc.shader.UniformProvider
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.ARBUniformBufferObject.GL_INVALID_INDEX
import org.lwjgl.opengl.ARBUniformBufferObject.glBindBufferBase
import org.lwjgl.opengl.GL41.*
import org.lwjgl.system.MemoryStack

internal data class GlShaderInstance(
    override val vertex: String,
    override val fragment: String,
    override val settings: ShaderSettings
) : ShaderInstance {

    override var id = 0
    private var vertexShaderId = 0
    private var fragmentShaderId = 0

    private val providers = mutableListOf<UniformProvider>()

    override fun compileShaders() {
        id = glCreateProgram()

        vertexShaderId = createShader(vertex, GL_VERTEX_SHADER)
        fragmentShaderId = createShader(fragment, GL_FRAGMENT_SHADER)

        link()
    }

    override fun bind() {
        glUseProgram(id)
        GlRenderSystem.shader = this

        providers.forEach { it.provide(this) }
    }

    override fun unbind() {
        glUseProgram(0)

        GlRenderSystem.shader = EmptyShaderInstance
    }

    override fun cleanup() {
        glDeleteProgram(id)
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

    override fun uploadUniformBuffer(name: String, buffer: UniformBuffer) {
        val blockIndex = glGetUniformBlockIndex(id, name)
        if (blockIndex != GL_INVALID_INDEX) {
            glUniformBlockBinding(id, blockIndex, 0)
            glBindBufferBase(GL_UNIFORM_BUFFER, 0, buffer.id)
        }
    }

    private fun uniform(name: String, value: Int.() -> Unit) {
        val location: Int = glGetUniformLocation(id, name)

        if(location != -1) {
            value.invoke(location)
        }
    }

    private fun createShader(shaderCode: String, shaderType: Int): Int {
        val shaderId = glCreateShader(shaderType)
        if (shaderId == 0) {
            throw RuntimeException("Error creating shader. Type: $shaderType")
        }

        glShaderSource(shaderId, shaderCode)
        glCompileShader(shaderId)

        check(glGetShaderi(shaderId, GL_COMPILE_STATUS) != 0) {
            "Error compiling Shader code: ${glGetShaderInfoLog(shaderId, 1024)}"
        }

        glAttachShader(id, shaderId)

        return shaderId
    }

    private fun link() {
        glLinkProgram(id)

        check(glGetProgrami(id, GL_LINK_STATUS) != 0) {
            "Error linking Shader code: ${glGetProgramInfoLog(id, 1024)}"
        }

        glDetachShader(
            id,
            vertexShaderId
        )
        glDetachShader(
            id,
            fragmentShaderId
        )


        glDeleteShader(
            vertexShaderId
        )
        glDeleteShader(
            fragmentShaderId
        )
    }


    object Factory : ShaderInstance.Factory {
        override fun create(
            vertexShader: StringAsset,
            fragmentShader: StringAsset,
            shaderSettings: ShaderSettings
        ): ShaderInstance {
            return GlShaderInstance(vertexShader.text, fragmentShader.text, shaderSettings)
        }
    }

}