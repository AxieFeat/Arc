package arc.gl.graphics


import arc.graphics.Drawer
import arc.graphics.vertex.VertexBuffer
import korlibs.math.geom.Matrix4

internal object GlDrawer : Drawer {

    private val emptyMatrix = Matrix4()

    override fun draw(buffer: VertexBuffer) {
        draw(emptyMatrix, buffer)
    }

    override fun draw(matrix: Matrix4, buffer: VertexBuffer) {
        GlRenderContext.shader.setUniform("modelMatrix", matrix)

        GlVertexUploader.draw(buffer)
    }
}
