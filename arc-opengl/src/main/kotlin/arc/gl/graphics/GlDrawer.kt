package arc.gl.graphics

import arc.graphics.DrawBuffer
import arc.graphics.Drawer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.shader.ShaderInstance

internal object GlDrawer : Drawer {

    override fun begin(mode: DrawerMode, format: VertexFormat, bufferSize: Int): DrawBuffer {
        return DrawBuffer.create(mode, format, bufferSize)
    }

    override fun draw(buffer: DrawBuffer) {
        if(buffer !is GlDrawBuffer) return

        GlVertexUploader.draw(buffer)
    }

}