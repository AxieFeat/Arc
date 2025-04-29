package arc.gl.graphics.vertex

import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexArrayBuffer
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexType
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

internal data class GlVertexArrayBuffer(
    override val mode: DrawerMode,
    override val format: VertexFormat,
    private val buffer: ByteBuffer,
    override var vertices: Int,
    override val size: Int = buffer.capacity(),
) : VertexArrayBuffer {

    override var id: Int = glGenBuffers()
    override var bindingId: Int = glGenVertexArrays()

    init {
        bind()

        glBindBuffer(GL_ARRAY_BUFFER, id)
        glBufferData(GL_ARRAY_BUFFER, size.toLong(), GL_STREAM_DRAW)
        setupVertexAttributes()

        unbind()

        write(buffer, vertices)
    }

    override fun bind() {
        glBindVertexArray(bindingId)
    }

    override fun unbind() {
        glBindVertexArray(0)
    }

    private fun setupVertexAttributes() {
        var offset = 0

        for ((index, element) in format.elements.withIndex()) {
            glEnableVertexAttribArray(index)

            when (element.type) {
                VertexType.FLOAT -> glVertexAttribPointer(index, element.count, GL_FLOAT, false, format.nextOffset, offset.toLong())
                VertexType.UINT, VertexType.INT -> glVertexAttribIPointer(index, element.count, GL_INT, format.nextOffset, offset.toLong())
                VertexType.USHORT, VertexType.SHORT -> glVertexAttribPointer(index, element.count, GL_SHORT, false, format.nextOffset, offset.toLong())
                VertexType.UBYTE, VertexType.BYTE -> glVertexAttribPointer(index, element.count, GL_UNSIGNED_BYTE, true, format.nextOffset, offset.toLong())
            }

            offset += element.size
        }
    }

    override fun write(buffer: ByteBuffer, vertices: Int) {
        this.vertices = vertices

        glBindBuffer(GL_ARRAY_BUFFER, id)

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

        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    override fun cleanup() {
        if (bindingId >= 0) {
            glDeleteVertexArrays(bindingId)
            bindingId = -1
        }
        if (id >= 0) {
            glDeleteBuffers(id)
            id = -1
        }
    }

    object Factory : VertexArrayBuffer.Factory {
        override fun create(mode: DrawerMode, format: VertexFormat, buffer: ByteBuffer, vertices: Int): VertexArrayBuffer {
            return GlVertexArrayBuffer(mode, format, buffer, vertices)
        }
    }

}