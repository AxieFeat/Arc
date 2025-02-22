package arc.gl

import arc.Configuration
import arc.Engine
import arc.gl.graphics.GlRenderSystem
import arc.graphics.RenderSystem
import arc.window.Window

class GlEngine(
    configuration: Configuration
) : Engine {

    override val window: Window = GlApplication.window

    override val fps: Int = 0

    override val renderSystem: RenderSystem = GlRenderSystem

    override var fpsLimit: Int = configuration.windowRefreshRate

    override var isSkipRendering: Boolean = false

}