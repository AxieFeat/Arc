package arc.gl.graphics

import arc.graphics.DrawBuffer
import arc.graphics.Drawer
import arc.graphics.DrawerMode
import arc.graphics.VertexBuffer
import arc.graphics.vertex.VertexFormat
import arc.shader.ShaderInstance

internal object GlDrawer : Drawer {

    override fun begin(mode: DrawerMode, format: VertexFormat, bufferSize: Int): DrawBuffer {
        return DrawBuffer.create(mode, format, bufferSize)
    }

    override fun draw(buffer: VertexBuffer) {
        if(buffer !is GlVertexBuffer) return

        GlVertexUploader.draw(buffer)
    }

}