package arc.gl.graphics.vertex

import arc.gl.graphics.GlContextResource
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat
import org.lwjgl.opengl.GL11.glGetInteger
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER_BINDING
import org.lwjgl.opengl.GL15.GL_STREAM_DRAW
import org.lwjgl.opengl.GL15.glBindBuffer
import org.lwjgl.opengl.GL15.glBufferData
import org.lwjgl.opengl.GL15.glBufferSubData
import org.lwjgl.opengl.GL15.glDeleteBuffers
import org.lwjgl.opengl.GL15.glGenBuffers
import java.nio.ByteBuffer

internal class GlVertexBuffer(
    override val mode: DrawerMode,
    override val format: VertexFormat,
    buffer: ByteBuffer,
    override var vertices: Int,
    override var size: Int = buffer.remaining(),
) : GlContextResource(), VertexBuffer {

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
            return GlVertexBuffer(mode, format, buffer, vertices)
        }
    }
}
