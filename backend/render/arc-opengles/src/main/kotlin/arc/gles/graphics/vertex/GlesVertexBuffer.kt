package arc.gles.graphics.vertex

import arc.gles.graphics.GlesContextResource
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat
import org.lwjgl.opengles.GLES20.GL_ARRAY_BUFFER
import org.lwjgl.opengles.GLES20.GL_ARRAY_BUFFER_BINDING
import org.lwjgl.opengles.GLES20.GL_STREAM_DRAW
import org.lwjgl.opengles.GLES20.glBindBuffer
import org.lwjgl.opengles.GLES20.glBufferData
import org.lwjgl.opengles.GLES20.glBufferSubData
import org.lwjgl.opengles.GLES20.glDeleteBuffers
import org.lwjgl.opengles.GLES20.glGenBuffers
import org.lwjgl.opengles.GLES20.glGetInteger
import java.nio.ByteBuffer

internal class GlesVertexBuffer(
    override val mode: DrawerMode,
    override val format: VertexFormat,
    buffer: ByteBuffer,
    override var vertices: Int,
    override var size: Int = buffer.remaining(),
) : GlesContextResource(), VertexBuffer {

    override var id: Int = glGenBuffers()

    init {
        try {
            write(buffer, vertices)
        } catch (failure: Throwable) {
            cleanup()
            throw failure
        }
    }

    override fun bind() {
        requireCurrentContext()
        check(id >= 0) { "Vertex buffer is closed" }
        glBindBuffer(GL_ARRAY_BUFFER, id)
    }

    override fun unbind() {
        requireCurrentContext()
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    override fun write(buffer: ByteBuffer, vertices: Int) {
        requireCurrentContext()
        check(id >= 0) { "Vertex buffer is closed" }
        require(vertices >= 0) { "Vertex count must be nonnegative" }
        val bytes = Math.multiplyExact(vertices, format.nextOffset)
        require(buffer.isDirect) { "Vertex upload requires a direct buffer" }
        require(bytes <= buffer.remaining()) { "Vertex data requires $bytes initialized bytes, only ${buffer.remaining()} remain" }
        if (bytes > size) size = maxOf(bytes, (size.toLong() * 2).coerceAtMost(Int.MAX_VALUE.toLong()).toInt())
        val previous = glGetInteger(GL_ARRAY_BUFFER_BINDING)
        try {
            bind()
            // Orphan storage before writing. The driver retains the old allocation while in-flight draws need it.
            glBufferData(GL_ARRAY_BUFFER, size.toLong(), GL_STREAM_DRAW)
            if (bytes > 0) {
                val active = buffer.duplicate().limit(buffer.position() + bytes)
                glBufferSubData(GL_ARRAY_BUFFER, 0, active)
            }
            this.vertices = vertices
        } finally {
            glBindBuffer(GL_ARRAY_BUFFER, previous)
        }
    }

    override fun cleanup() {
        if (id >= 0) {
            if (isCurrentContext()) glDeleteBuffers(id)
            id = -1
        }
    }

    object Factory : VertexBuffer.Factory {

        override fun create(mode: DrawerMode, format: VertexFormat, buffer: ByteBuffer, vertices: Int): VertexBuffer {
            return GlesVertexBuffer(mode, format, buffer, vertices)
        }
    }
}
