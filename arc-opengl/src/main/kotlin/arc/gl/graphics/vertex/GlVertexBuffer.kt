package arc.gl.graphics.vertex

import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat
import org.lwjgl.opengl.GL41.*
import org.lwjgl.system.MemoryUtil

internal data class GlVertexBuffer(
    override val size: Int,
    override val mode: DrawerMode,
    override val format: VertexFormat,
) : VertexBuffer {


    override var id: Int = glGenBuffers()
    override var vertices: Int = 0

    override fun bind() {
        glBindBuffer(GL_ARRAY_BUFFER, id)
    }

    override fun unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    init {
        bind()
        glBufferData(GL_ARRAY_BUFFER, size * 4L, GL_STREAM_DRAW)
        unbind()
    }

    override fun write(drawBuffer: DrawBuffer) {
        vertices = drawBuffer.vertexCount

        bind()

        val ptr = glMapBufferRange(
            GL_ARRAY_BUFFER, 0, drawBuffer.byteBuffer.capacity().toLong(),
            GL_MAP_WRITE_BIT or GL_MAP_UNSYNCHRONIZED_BIT
        )

        if (ptr != null) {
            MemoryUtil.memCopy(
                MemoryUtil.memAddress(drawBuffer.byteBuffer),
                MemoryUtil.memAddress(ptr),
                drawBuffer.byteBuffer.capacity().toLong()
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
        override fun create(drawBuffer: DrawBuffer): VertexBuffer {
            return GlVertexBuffer(drawBuffer.bufferSize, drawBuffer.mode, drawBuffer.format).also {
                it.write(drawBuffer)
            }
        }

    }

}