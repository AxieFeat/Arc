package arc.gl.graphics.vertex

import arc.gl.graphics.GlContextResource
import arc.gl.graphics.GlRenderContext
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexArrayBuffer
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexType
import java.nio.ByteBuffer
import org.lwjgl.opengl.GL11.GL_FLOAT
import org.lwjgl.opengl.GL11.GL_INT
import org.lwjgl.opengl.GL11.GL_SHORT
import org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glDeleteVertexArrays
import org.lwjgl.opengl.GL30.glGenVertexArrays
import org.lwjgl.opengl.GL30.glVertexAttribIPointer

internal class GlVertexArrayBuffer(
    override val mode: DrawerMode,
    override val format: VertexFormat,
    buffer: ByteBuffer,
    vertices: Int,
) : GlContextResource(), VertexArrayBuffer {

    private val data = GlVertexBuffer(mode, format, buffer, vertices)
    override val id get() = data.id
    override val size get() = data.size
    override val vertices get() = data.vertices
    override var bindingId = glGenVertexArrays()
        private set

    init {
        try {
            GlRenderContext.beginScopedPass(emptySet()).use {
                bind()
                data.bind()
                var offset = 0
                format.elements.forEachIndexed { index, element ->
                    glEnableVertexAttribArray(index)
                    when (element.type) {
                        VertexType.FLOAT -> glVertexAttribPointer(index, element.count, GL_FLOAT, false, format.nextOffset, offset.toLong())
                        VertexType.UINT, VertexType.INT -> glVertexAttribIPointer(index, element.count, GL_INT, format.nextOffset, offset.toLong())
                        VertexType.USHORT, VertexType.SHORT -> glVertexAttribPointer(
                            index, element.count, GL_SHORT, false, format.nextOffset, offset.toLong()
                        )

                        VertexType.UBYTE, VertexType.BYTE -> glVertexAttribPointer(
                            index, element.count, GL_UNSIGNED_BYTE, true, format.nextOffset, offset.toLong()
                        )
                    }
                    offset += element.size
                }
            }
        } catch (failure: Throwable) {
            cleanup()
            throw failure
        }
    }

    override fun bind() {
        requireCurrentContext()
        check(bindingId >= 0) { "Vertex array is closed" }
        glBindVertexArray(bindingId)
    }

    override fun unbind() {
        requireCurrentContext()
        glBindVertexArray(0)
    }

    override fun write(buffer: ByteBuffer, vertices: Int) = data.write(buffer, vertices)

    override fun cleanup() {
        if (bindingId >= 0) {
            if (isCurrentContext()) glDeleteVertexArrays(bindingId)
            bindingId = -1
        }
        data.cleanup()
    }

    object Factory : VertexArrayBuffer.Factory {

        override fun create(mode: DrawerMode, format: VertexFormat, buffer: ByteBuffer, vertices: Int): VertexArrayBuffer =
            GlVertexArrayBuffer(mode, format, buffer, vertices)
    }
}
