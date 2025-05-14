package arc.gl.graphics

import arc.graphics.DrawBuffer
import arc.graphics.Drawer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat
import org.joml.Matrix4f

internal object GlDrawer : Drawer {

    private val emptyMatrix = Matrix4f()

    override fun begin(mode: DrawerMode, format: VertexFormat, bufferSize: Int): DrawBuffer {
        return DrawBuffer.of(mode, format, bufferSize)
    }

    override fun draw(buffer: VertexBuffer) {
        draw(emptyMatrix, buffer)
    }

    override fun draw(matrix: Matrix4f, buffer: VertexBuffer) {
        GlRenderSystem.shader.setUniform("modelMatrix", matrix)

        GlVertexUploader.draw(buffer)
    }

}