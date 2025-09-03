package arc.gles.graphics

import arc.graphics.vertex.VertexArrayBuffer
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexType
import org.lwjgl.opengles.GLES20.GL_FLOAT
import org.lwjgl.opengles.GLES20.GL_INT
import org.lwjgl.opengles.GLES20.GL_SHORT
import org.lwjgl.opengles.GLES20.GL_UNSIGNED_BYTE
import org.lwjgl.opengles.GLES20.glBindAttribLocation
import org.lwjgl.opengles.GLES20.glDisableVertexAttribArray
import org.lwjgl.opengles.GLES20.glDrawArrays
import org.lwjgl.opengles.GLES20.glEnableVertexAttribArray
import org.lwjgl.opengles.GLES20.glUseProgram
import org.lwjgl.opengles.GLES20.glVertexAttribPointer
import org.lwjgl.opengles.GLES30.glBindVertexArray
import org.lwjgl.opengles.GLES30.glGenVertexArrays
import org.lwjgl.opengles.GLES30.glVertexAttribIPointer

internal object GlesVertexUploader {

    private val vao = glGenVertexArrays()

    @JvmStatic
    @Throws(RuntimeException::class)
    fun draw(vertexBuffer: VertexBuffer) {
        if (vertexBuffer.vertices == 0) return

        if (vertexBuffer is VertexArrayBuffer) {
            vertexBuffer.bind()
            glDrawArrays(vertexBuffer.mode.id, 0, vertexBuffer.vertices)
            return
        }

        glBindVertexArray(vao)
        vertexBuffer.bind()

        var offset = 0
        for ((index, element) in vertexBuffer.format.elements.withIndex()) {
            glBindAttribLocation(GlesRenderSystem.shader.id, index, element.name)

            glEnableVertexAttribArray(index)
            when (element.type) {
                VertexType.FLOAT -> glVertexAttribPointer(index, element.count, GL_FLOAT, false, vertexBuffer.format.nextOffset, offset.toLong())
                VertexType.UINT, VertexType.INT -> glVertexAttribIPointer(index, element.count, GL_INT, vertexBuffer.format.nextOffset, offset.toLong())
                VertexType.USHORT, VertexType.SHORT -> glVertexAttribPointer(index, element.count, GL_SHORT, false, vertexBuffer.format.nextOffset, offset.toLong())
                VertexType.UBYTE, VertexType.BYTE -> glVertexAttribPointer(index, element.count, GL_UNSIGNED_BYTE, true, vertexBuffer.format.nextOffset, offset.toLong())
            }
            offset += element.size
        }

        glDrawArrays(vertexBuffer.mode.id, 0, vertexBuffer.vertices)

        for (i in vertexBuffer.format.elements.indices) {
            glDisableVertexAttribArray(i)
        }

        glBindVertexArray(0)
        glUseProgram(0)
    }
}


