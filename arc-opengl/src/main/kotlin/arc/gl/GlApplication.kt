package arc.gl

import arc.*
import arc.files.ArcLocationSpace
import arc.files.LocationSpace
import arc.gl.graphics.GlDrawer
import arc.gl.graphics.GlRenderSystem
import arc.graphics.Drawer
import arc.graphics.RenderSystem
import arc.input.InputDevice
import arc.window.EmptyWindowHandler
import arc.window.Window
import org.lwjgl.opengl.GL
import java.io.File

object GlApplication : AbstractApplication() {

    override val platform: Platform = GlPlatform

    override lateinit var window: Window
    override lateinit var renderSystem: RenderSystem
    override val locationSpace: LocationSpace = ArcLocationSpace
    override lateinit var inputDevices: Set<InputDevice>

    override fun init(configuration: Configuration) {
        this.window = Window.create(
            name = configuration.windowName,
            handler = EmptyWindowHandler,
            height = configuration.windowHeight,
            width = configuration.windowWidth
        )

        this.renderSystem = GlRenderSystem

        window.create()

        GL.createCapabilities()
    }

    override fun screenshot(folder: File, name: String) {

    }

    override fun close() {
        window.close()
    }

    /**
     * Initialize this application in factory.
     */
    @JvmStatic
    fun preload() {
        ArcApplicationFactory.register(GlPlatform.id) {
            GlFactoryProvider.bootstrap()

            return@register GlApplication
        }
    }
}