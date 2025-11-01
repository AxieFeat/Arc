package arc.gles.graphics.vertex

import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat
import org.lwjgl.opengles.GLES20.GL_ARRAY_BUFFER
import org.lwjgl.opengles.GLES20.GL_DYNAMIC_DRAW
import org.lwjgl.opengles.GLES20.glBindBuffer
import org.lwjgl.opengles.GLES20.glBufferData
import org.lwjgl.opengles.GLES20.glDeleteBuffers
import org.lwjgl.opengles.GLES20.glGenBuffers
import org.lwjgl.opengles.GLES30.GL_MAP_UNSYNCHRONIZED_BIT
import org.lwjgl.opengles.GLES30.GL_MAP_WRITE_BIT
import org.lwjgl.opengles.GLES30.glMapBufferRange
import org.lwjgl.opengles.GLES30.glUnmapBuffer
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

internal class GlesVertexBuffer(
    override val mode: DrawerMode,
    override val format: VertexFormat,
    private val buffer: ByteBuffer,
    override var vertices: Int,
    override val size: Int = buffer.capacity(),
) : VertexBuffer {

    override var id: Int = glGenBuffers()

    init {
        bind()
        glBufferData(GL_ARRAY_BUFFER, size.toLong(), GL_DYNAMIC_DRAW)
        unbind()

        write(buffer, vertices)
    }

    override fun bind() {
        glBindBuffer(GL_ARRAY_BUFFER, id)
    }

    override fun unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0)
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
            return GlesVertexBuffer(mode, format, buffer, vertices)
        }

    }
}
