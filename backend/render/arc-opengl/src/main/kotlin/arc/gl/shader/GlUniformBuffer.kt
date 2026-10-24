package arc.gl.shader

import arc.gl.graphics.GlContextResource
import arc.shader.UniformBuffer
import java.nio.ByteBuffer
import org.lwjgl.opengl.GL11.glGetInteger
import org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW
import org.lwjgl.opengl.GL15.glBindBuffer
import org.lwjgl.opengl.GL15.glBufferData
import org.lwjgl.opengl.GL15.glBufferSubData
import org.lwjgl.opengl.GL15.glDeleteBuffers
import org.lwjgl.opengl.GL15.glGenBuffers
import org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER
import org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER_BINDING
import org.lwjgl.system.MemoryUtil

internal class GlUniformBuffer(override val size: Int) : GlContextResource(), UniformBuffer {

    override var id = 0
        private set
    private val cachedBuffer = MemoryUtil.memAlloc(size.also { require(it >= 0) { "Uniform buffer size must be nonnegative" } })
    private var closed = false

    init {
        id = glGenBuffers()
        val previous = glGetInteger(GL_UNIFORM_BUFFER_BINDING)
        try {
            bind()
            glBufferData(GL_UNIFORM_BUFFER, size.toLong(), GL_DYNAMIC_DRAW)
        } catch (failure: Throwable) {
            cleanup()
            throw failure
        } finally {
            glBindBuffer(GL_UNIFORM_BUFFER, previous)
        }
    }

    override fun update(data: ByteBuffer) {
        requireCurrentContext()
        check(!closed) { "Uniform buffer is closed" }
        require(data.remaining() <= size) { "Uniform update exceeds capacity $size" }
        cachedBuffer.clear()
        cachedBuffer.put(data.duplicate())
        cachedBuffer.flip()
        val previous = glGetInteger(GL_UNIFORM_BUFFER_BINDING)
        try {
            bind()
            glBufferSubData(GL_UNIFORM_BUFFER, 0, cachedBuffer)
        } finally {
            glBindBuffer(GL_UNIFORM_BUFFER, previous)
        }
    }

    override fun update(size: Int, data: ByteBuffer.() -> Unit) {
        require(size in 0..this.size) { "Uniform update size exceeds capacity" }
        val temporary = MemoryUtil.memAlloc(size)
        try {
            temporary.apply(data).flip()
            update(temporary)
        } finally {
            MemoryUtil.memFree(temporary)
        }
    }

    override fun bind() {
        requireCurrentContext()
        check(!closed) { "Uniform buffer is closed" }
        glBindBuffer(GL_UNIFORM_BUFFER, id)
    }

    override fun unbind() {
        requireCurrentContext()
        glBindBuffer(GL_UNIFORM_BUFFER, 0)
    }

    override fun cleanup() {
        if (closed) return
        if (isCurrentContext()) glDeleteBuffers(id)
        MemoryUtil.memFree(cachedBuffer)
        id = 0
        closed = true
    }

    object Factory : UniformBuffer.Factory {

        override fun create(size: Int): UniformBuffer = GlUniformBuffer(size)
    }
}
