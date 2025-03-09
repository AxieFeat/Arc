package arc.gl.graphics

import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexType
import org.lwjgl.opengl.GL11.GL_FLOAT
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.glBindBuffer
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*

internal object GlVertexUploader {

    private val vao = glGenVertexArrays()

    @JvmStatic
    @Throws(RuntimeException::class)
    fun draw(drawBuffer: GlDrawBuffer) {
        if (!drawBuffer.isEnded || drawBuffer.vertexCount == 0) return

        glBindVertexArray(vao)

        glBindBuffer(GL_ARRAY_BUFFER, drawBuffer.vbo)

        var offset = 0
        for ((index, element) in drawBuffer.format.elements.withIndex()) {
            glEnableVertexAttribArray(index)

            when (element.type) {
                VertexType.FLOAT -> glVertexAttribPointer(index, element.count, GL_FLOAT, false, drawBuffer.format.nextOffset, offset.toLong())
                VertexType.UINT, VertexType.INT -> glVertexAttribIPointer(index, element.count, GL_INT, drawBuffer.format.nextOffset, offset.toLong())
                VertexType.USHORT, VertexType.SHORT -> glVertexAttribPointer(index, element.count, GL_SHORT, false, drawBuffer.format.nextOffset, offset.toLong())
                VertexType.UBYTE, VertexType.BYTE -> glVertexAttribPointer(index, element.count, GL_UNSIGNED_BYTE, true, drawBuffer.format.nextOffset, offset.toLong())
            }

            offset += element.size
        }

        val glMode = when (drawBuffer.mode) {
            DrawerMode.TRIANGLES -> GL_TRIANGLES
            DrawerMode.LINES -> GL_LINES
            DrawerMode.TRIANGLE_STRIP -> GL_TRIANGLE_STRIP
            DrawerMode.LINE_STRIP -> GL_LINE_STRIP
            DrawerMode.TRIANGLE_FAN -> GL_TRIANGLE_FAN
            DrawerMode.QUADS -> GL_QUADS

            else -> GL_TRIANGLES
        }

        glDrawArrays(glMode, 0, drawBuffer.vertexCount)

        for (i in drawBuffer.format.elements.indices) {
            glDisableVertexAttribArray(i)
        }

        glBindVertexArray(0)

        glDeleteVertexArrays(vao)
    }

}