package arc.gl.shader

import arc.shader.UniformBuffer
import org.lwjgl.opengl.ARBUniformBufferObject.GL_UNIFORM_BUFFER
import org.lwjgl.opengl.GL41.*
import org.lwjgl.system.MemoryUtil.memAlloc
import org.lwjgl.system.MemoryUtil.memFree
import java.nio.ByteBuffer

internal class GlUniformBuffer(
    override val size: Int
) : UniformBuffer {

    override val id: Int = glGenBuffers()
    private val cachedBuffer = memAlloc(size)

    init {
        bind()
        glBufferData(GL_UNIFORM_BUFFER, size.toLong(), GL_DYNAMIC_DRAW)
        unbind()
    }

    override fun update(data: ByteBuffer) {
        require(data.remaining() <= size) { "Data size (${data.remaining()}) > buffer capacity ($size)"  }

        cachedBuffer.clear()
        cachedBuffer.put(data)
        cachedBuffer.flip()

        bind()
        glBufferSubData(GL_UNIFORM_BUFFER, 0, cachedBuffer)
        unbind()
    }

    override fun update(size: Int, data: ByteBuffer.() -> Unit) {
        val buffer = memAlloc(size).apply(data).flip()
        try {
            update(buffer)
        } finally {
            memFree(buffer)
        }
    }

    override fun cleanup() {
        memFree(cachedBuffer)
        glDeleteBuffers(id)
    }

    override fun bind() = glBindBuffer(GL_UNIFORM_BUFFER, id)
    override fun unbind() =glBindBuffer(GL_UNIFORM_BUFFER, 0)

    object Factory : UniformBuffer.Factory {
        override fun create(size: Int): UniformBuffer {
            return GlUniformBuffer(size)
        }
    }

}