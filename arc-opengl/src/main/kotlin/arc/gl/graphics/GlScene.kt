package arc.gl.graphics

import arc.graphics.ArcScene
import arc.graphics.Drawer
import arc.graphics.Scene

internal class GlScene(
    override val drawer: Drawer = GlDrawer
) : ArcScene() {

    override val fps: Int = 0
    override val delta: Float = 0f

    object Factory : Scene.Factory {

        override fun create(drawer: Drawer): Scene = GlScene(drawer)

    }

}