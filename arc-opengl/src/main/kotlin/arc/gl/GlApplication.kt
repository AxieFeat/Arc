package arc.gl

import arc.AbstractApplication
import arc.Application
import arc.ApplicationBackend
import arc.gl.graphics.GlRenderSystem
import arc.gl.window.GlfwGlWindow
import arc.graphics.RenderSystem
import arc.window.EmptyWindowHandler
import arc.window.Window
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL41.*
import org.lwjgl.system.MemoryUtil
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

internal object GlApplication : AbstractApplication() {

    override val backend: ApplicationBackend = GlApplicationBackend

    override lateinit var window: GlfwGlWindow
    override lateinit var renderSystem: RenderSystem

    override fun init() {
        this.window = Window.of(
            name = "",
            handler = EmptyWindowHandler,
            height = 420,
            width = 720
        ) as? GlfwGlWindow ?: throw IllegalStateException("Window is not GlfwGlWindow. Why?")

        this.renderSystem = GlRenderSystem

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