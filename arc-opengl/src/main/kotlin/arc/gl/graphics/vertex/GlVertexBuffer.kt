package arc.gl.graphics.vertex

import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat
import org.lwjgl.opengl.GL41.*
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

internal data class GlVertexBuffer(
    override val mode: DrawerMode,
    override val format: VertexFormat,
    private val buffer: ByteBuffer,
    override var vertices: Int,
    override val size: Int = buffer.capacity(),
) : VertexBuffer {

    override var id: Int = glGenBuffers()

    override fun bind() {
        glBindBuffer(GL_ARRAY_BUFFER, id)
    }

    override fun unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    init {
        bind()
        glBufferData(GL_ARRAY_BUFFER, size.toLong(), GL_DYNAMIC_DRAW)
        unbind()

        write(buffer, vertices)
    }

    override fun write(buffer: ByteBuffer, vertices: Int) {
        this.vertices = vertices

        bind()

        val ptr = glMapBufferRange(
            GL_ARRAY_BUFFER, 0, buffer.capacity().toLong(),
            GL_MAP_WRITE_BIT or GL_MAP_UNSYNCHRONIZED_BIT
        )

        if (ptr != null) {
            MemoryUtil.memCopy(
                MemoryUtil.memAddress(buffer),
                MemoryUtil.memAddress(ptr),
                buffer.capacity().toLong()
            )
            glUnmapBuffer(GL_ARRAY_BUFFER)
        }

        unbind()
    }

    override fun cleanup() {
        if(id >= 0) {
            glDeleteBuffers(id)
            id = -1
        }
    }

    object Factory : VertexBuffer.Factory {
        override fun create(mode: DrawerMode, format: VertexFormat, buffer: ByteBuffer, vertices: Int): VertexBuffer {
            return GlVertexBuffer(mode, format, buffer, vertices)
        }

    }

}