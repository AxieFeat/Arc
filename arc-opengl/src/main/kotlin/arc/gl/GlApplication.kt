package arc.gl

import arc.AbstractApplication
import arc.Application
import arc.ApplicationBackend
import arc.gl.graphics.GlRenderSystem
import arc.gl.window.GlfwGlWindow
import arc.graphics.RenderSystem
import arc.window.EmptyWindowHandler
import arc.window.Window
import arc.window.WindowException
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.GL_FRONT
import org.lwjgl.opengl.GL11.GL_RGBA
import org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE
import org.lwjgl.opengl.GL11.glReadBuffer
import org.lwjgl.opengl.GL11.glReadPixels
import org.lwjgl.system.MemoryUtil
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

internal object GlApplication : AbstractApplication() {

    override val backend: ApplicationBackend = GlApplicationBackend

    private var _window: GlfwGlWindow? = null
    private var _renderSystem: GlRenderSystem? = null

    override val window: GlfwGlWindow
        get() = checkNotNull(_window) {
            "Window is not initialized yet. Accessing it before Application.init() is not allowed." }
    override val renderSystem: RenderSystem
        get() = checkNotNull(_renderSystem) {
            "RenderSystem is not initialized yet. Accessing it before Application.init() is not allowed."
        }

    override fun init() {
        _window = Window.of(
            name = "",
            handler = EmptyWindowHandler,
            width = 720,
            height = 420
        ) as? GlfwGlWindow ?: throw WindowException("Window is not GlfwGlWindow. Why?")

        _renderSystem = GlRenderSystem

        window.create()

        GL.createCapabilities()
    }

    override fun screenshot(folder: File, name: String) {
        val screenshotFile = File(folder, name)

        check(!screenshotFile.exists()) { "Can not take screenshot. File with name '$name' already exists!" }

        screenshotFile.createNewFile()

        val width = window.height
        val height = window.width

        val bufferSize = width * height * 4
        val buffer = MemoryUtil.memAlloc(bufferSize)

        glReadBuffer(GL_FRONT)
        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer)

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

    internal object Provider : Application.Provider {
        override fun provide(): Application {
            return GlApplication
        }
    }
}
