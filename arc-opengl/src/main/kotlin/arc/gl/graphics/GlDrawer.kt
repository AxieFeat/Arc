package arc.gl.graphics

import arc.graphics.Drawer
import arc.graphics.vertex.VertexBuffer
import org.joml.Matrix4f

internal object GlDrawer : Drawer {

    private val emptyMatrix = Matrix4f()

    override fun draw(buffer: VertexBuffer) {
        draw(emptyMatrix, buffer)
    }

    override fun draw(matrix: Matrix4f, buffer: VertexBuffer) {
        GlRenderSystem.shader.setUniform("modelMatrix", matrix)

        GlVertexUploader.draw(buffer)
    }
}
