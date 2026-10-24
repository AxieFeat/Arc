package arc.gl.shader

import arc.asset.StringAsset
import arc.gl.graphics.GlContextResource
import arc.gl.graphics.GlRenderContext
import arc.graphics.EmptyShaderInstance
import arc.graphics.vertex.VertexType
import arc.shader.ShaderInstance
import arc.shader.ShaderSettings
import arc.shader.UniformBuffer
import arc.shader.UniformProvider
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.ARBUniformBufferObject.GL_INVALID_INDEX
import org.lwjgl.opengl.ARBUniformBufferObject.GL_UNIFORM_BUFFER
import org.lwjgl.opengl.ARBUniformBufferObject.glBindBufferBase
import org.lwjgl.opengl.ARBUniformBufferObject.glGetUniformBlockIndex
import org.lwjgl.opengl.ARBUniformBufferObject.glUniformBlockBinding
import org.lwjgl.opengl.GL20.GL_COMPILE_STATUS
import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.GL_LINK_STATUS
import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import org.lwjgl.opengl.GL20.glAttachShader
import org.lwjgl.opengl.GL20.glCompileShader
import org.lwjgl.opengl.GL20.glCreateProgram
import org.lwjgl.opengl.GL20.glCreateShader
import org.lwjgl.opengl.GL20.glDeleteProgram
import org.lwjgl.opengl.GL20.glDeleteShader
import org.lwjgl.opengl.GL20.glDetachShader
import org.lwjgl.opengl.GL20.glGetProgramInfoLog
import org.lwjgl.opengl.GL20.glGetProgrami
import org.lwjgl.opengl.GL20.glGetShaderInfoLog
import org.lwjgl.opengl.GL20.glGetShaderi
import org.lwjgl.opengl.GL20.glGetUniformLocation
import org.lwjgl.opengl.GL20.glLinkProgram
import org.lwjgl.opengl.GL20.glShaderSource
import org.lwjgl.opengl.GL20.glUniform1f
import org.lwjgl.opengl.GL20.glUniform1i
import org.lwjgl.opengl.GL20.glUniform2f
import org.lwjgl.opengl.GL20.glUniform3f
import org.lwjgl.opengl.GL20.glUniform4f
import org.lwjgl.opengl.GL20.glUniformMatrix3fv
import org.lwjgl.opengl.GL20.glUniformMatrix4fv
import org.lwjgl.opengl.GL20.glUseProgram
import org.lwjgl.system.MemoryStack

@Suppress("MethodOverloading")
internal class GlShaderInstance(
    override val vertex: String,
    override val fragment: String,
    override val settings: ShaderSettings
) : GlContextResource(), ShaderInstance {

    override var id = 0
    private var vertexShaderId = 0
    private var fragmentShaderId = 0

    private val providers = mutableListOf<UniformProvider>()
    private val uniformLocations = mutableMapOf<String, Int>()

    override fun compileShaders() {
        requireCurrentContext()
        cleanup()
        id = glCreateProgram()
        try {
            vertexShaderId = createShader(vertex, GL_VERTEX_SHADER)
            fragmentShaderId = createShader(fragment, GL_FRAGMENT_SHADER)
            link()
        } catch (failure: Throwable) {
            if (vertexShaderId != 0) glDeleteShader(vertexShaderId)
            if (fragmentShaderId != 0) glDeleteShader(fragmentShaderId)
            cleanup()
            throw failure
        } finally {
            vertexShaderId = 0
            fragmentShaderId = 0
        }
    }

    override fun bind() {
        requireCurrentContext()
        check(id != 0) { "Shader is not compiled or is closed" }
        glUseProgram(id)
        GlRenderContext.shader = this

        providers.forEach { it.provide(this) }
    }

    override fun unbind() {
        requireCurrentContext()
        glUseProgram(0)

        GlRenderContext.shader = EmptyShaderInstance
    }

    override fun cleanup() {
        if (id != 0) {
            if (isCurrentContext()) glDeleteProgram(id)
            id = 0
        }
        uniformLocations.clear()
    }

    override fun addProvider(provider: UniformProvider) {
        providers.add(provider)
    }

    override fun setUniform(name: String, value: Boolean) {
        setUniform(name, if (value) 1 else 0)
    }

    override fun setUniform(name: String, value: Int) {
        uniform(name) { glUniform1i(this, value) }
    }

    override fun setUniform(name: String, value: Float) {
        uniform(name) { glUniform1f(this, value) }
    }

    override fun setUniform(name: String, value: Matrix3f) {
        uniform(name) {
            MemoryStack.stackPush().use { stack ->
                glUniformMatrix3fv(
                    this,
                    false,
                    value[stack.mallocFloat(MATRIX3F_SIZE)]
                )
            }
        }
    }

    override fun setUniform(name: String, value: Matrix4f) {
        uniform(name) {
            MemoryStack.stackPush().use { stack ->
                glUniformMatrix4fv(
                    this,
                    false,
                    value[stack.mallocFloat(MATRIX4F_SIZE)]
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
        requireCurrentContext()
        check(buffer.resourceGeneration == 0L || buffer.resourceGeneration == resourceGeneration) { "Uniform buffer belongs to an old context" }
        val blockIndex = glGetUniformBlockIndex(id, name)
        if (blockIndex != GL_INVALID_INDEX) {
            glUniformBlockBinding(id, blockIndex, 0)
            glBindBufferBase(GL_UNIFORM_BUFFER, 0, buffer.id)
        }
    }

    private fun uniform(name: String, value: Int.() -> Unit) {
        requireCurrentContext()
        check(id != 0) { "Shader is not compiled or is closed" }
        val location: Int = uniformLocations.getOrPut(name) { glGetUniformLocation(id, name) }

        if (location != -1) {
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

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            val message = glGetShaderInfoLog(shaderId, MAX_LOG_LENGTH)
            glDeleteShader(shaderId)
            error("Error compiling Shader code: $message")
        }

        glAttachShader(id, shaderId)

        return shaderId
    }

    private fun link() {
        glLinkProgram(id)

        check(glGetProgrami(id, GL_LINK_STATUS) != 0) {
            "Error linking Shader code: ${glGetProgramInfoLog(id, MAX_LOG_LENGTH)}"
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

    companion object {

        private val MATRIX3F_SIZE = VertexType.FLOAT.size * 3
        private val MATRIX4F_SIZE = VertexType.FLOAT.size * 4

        private const val MAX_LOG_LENGTH = 1024
    }
}
