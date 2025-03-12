package arc.gl.graphics

import arc.gl.GlApplication
import arc.graphics.vertex.VertexType
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.GL_FLOAT
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.glBindBuffer
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryStack

internal object GlVertexUploader {

    private val vao = glGenVertexArrays()

    private val view: Matrix4f
        get() = GlApplication.renderSystem.scene.camera.view
    private val projection: Matrix4f
        get() = GlApplication.renderSystem.scene.camera.projection

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

        applyMatrices()

        glDrawArrays(drawBuffer.mode.id, 0, drawBuffer.vertexCount)

        for (i in drawBuffer.format.elements.indices) {
            glDisableVertexAttribArray(i)
        }

        glBindVertexArray(0)
    }

    private fun applyTexture() {

    }

    private fun applyMatrices() {
        val projMat = glGetUniformLocation(GlRenderSystem.shader.id, "ProjMat")
        val viewMat = glGetUniformLocation(GlRenderSystem.shader.id, "ModelViewMat")

        if(projMat != -1) {
            uploadMatrix4f(projMat, projection)
        }
        if(viewMat != -1) {
            uploadMatrix4f(viewMat, view)
        }
    }

    private fun uploadMatrix4f(location: Int, matrix: Matrix4f) {
        MemoryStack.stackPush().use { stack ->
            val buffer = stack.mallocFloat(16)
            matrix.get(buffer)
            glUniformMatrix4fv(location, false, buffer)
        }
    }
}