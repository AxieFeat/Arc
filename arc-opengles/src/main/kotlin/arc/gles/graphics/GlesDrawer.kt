package arc.gles.graphics

import arc.graphics.Drawer
import arc.graphics.vertex.VertexBuffer
import org.joml.Matrix4f

internal object GlesDrawer : Drawer {

    private val emptyMatrix = Matrix4f()

    override fun draw(buffer: VertexBuffer) {
        draw(emptyMatrix, buffer)
    }

    override fun draw(matrix: Matrix4f, buffer: VertexBuffer) {
        GlesRenderSystem.shader.setUniform("modelMatrix", matrix)

        GlesVertexUploader.draw(buffer)
    }
}
