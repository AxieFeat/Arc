package arc.gl.graphics

import arc.graphics.DrawBuffer
import arc.graphics.Drawer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat

object GlDrawer : Drawer {

    override fun begin(mode: DrawerMode, format: VertexFormat): DrawBuffer {
        return GlDrawBuffer(mode, format)
    }

}