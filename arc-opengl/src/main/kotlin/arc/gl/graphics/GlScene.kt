package arc.gl.graphics

import arc.graphics.ArcScene
import arc.graphics.Scene

class GlScene : ArcScene() {

    override val fps: Int = 0
    override val delta: Float = 0f

    object Factory : Scene.Factory {
        override fun create(): Scene = GlScene()
    }

}