package arc.gl

import arc.AbstractApplication
import arc.Application
import arc.Configuration
import arc.Platform
import arc.files.ArcLocationSpace
import arc.files.LocationSpace
import arc.gl.graphics.GlRenderSystem
import arc.graphics.RenderSystem
import arc.input.ArcInput
import arc.window.EmptyWindowHandler
import arc.window.Window
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.GL_RGBA
import org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE
import org.lwjgl.opengl.GL41
import org.lwjgl.system.MemoryUtil
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object GlApplication : AbstractApplication() {

    override val platform: Platform = GlPlatform

    override lateinit var window: Window
    override lateinit var renderSystem: RenderSystem
    override val locationSpace: LocationSpace = ArcLocationSpace

    override fun init(configuration: Configuration) {
        this.window = Window.create(
            name = configuration.windowName,
            handler = EmptyWindowHandler,
            height = configuration.windowHeight,
            width = configuration.windowWidth
        )

        ArcInput.install(this)

        this.renderSystem = GlRenderSystem

        window.create()

        GL.createCapabilities()
    }

    override fun screenshot(folder: File, name: String) {
        val screenshotFile = File(folder, name)
        if (screenshotFile.exists()) throw IllegalArgumentException("Can not take screenshot. File with name '$name' already exists!")

        screenshotFile.createNewFile()

        val width = window.height
        val height = window.width

        val bufferSize = width * height * 4
        val buffer = MemoryUtil.memAlloc(bufferSize)

        GL41.glReadBuffer(GL41.GL_FRONT)
        GL41.glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer)

        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val i = (x + (height - y - 1) * width) * 4
                val r = buffer.get(i).toInt() and 0xFF
                val g = buffer.get(i + 1).toInt() and 0xFF
                val b = buffer.get(i + 2).toInt() and 0xFF
                val a = buffer.get(i + 3).toInt() and 0xFF
                val argb = (a shl 24) or (r shl 16) or (g shl 8) or b
                image.setRGB(x, y, argb)
            }
        }

        MemoryUtil.memFree(buffer)

        ImageIO.write(image, "png", screenshotFile)
    }

    override fun close() {
        window.close()
    }

    /**
     * Initialize this application in factory.
     */
    @JvmStatic
    fun preload() {
        GlFactoryProvider.bootstrap()
    }

    internal object Factory : Application.Factory {
        override fun create(): Application {
            return GlApplication
        }

    }
}