package arc.gl.graphics

import arc.gl.graphics.vertex.GlVertexBuffer
import arc.graphics.DrawBuffer
import arc.graphics.Drawer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat

internal object GlDrawer : Drawer {

    override fun begin(mode: DrawerMode, format: VertexFormat, bufferSize: Int): DrawBuffer {
        return DrawBuffer.create(mode, format, bufferSize)
    }

    override fun draw(buffer: VertexBuffer) {
        if(buffer !is GlVertexBuffer) return

        GlVertexUploader.draw(buffer)
    }

}