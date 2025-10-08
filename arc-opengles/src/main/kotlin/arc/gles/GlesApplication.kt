package arc.gles

import arc.AbstractApplication
import arc.Application
import arc.ApplicationBackend
import arc.gles.graphics.GlesRenderSystem
import arc.gles.window.EglGlesWindow
import arc.graphics.RenderSystem
import arc.window.EmptyWindowHandler
import arc.window.Window
import org.lwjgl.opengles.GLES
import org.lwjgl.opengles.GLES20.GL_FRONT
import org.lwjgl.opengles.GLES20.GL_RGBA
import org.lwjgl.opengles.GLES20.GL_UNSIGNED_BYTE
import org.lwjgl.opengles.GLES20.glReadPixels
import org.lwjgl.opengles.GLES30.glReadBuffer
import org.lwjgl.system.MemoryUtil
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

internal object GlesApplication : AbstractApplication() {

    override val backend: ApplicationBackend = GlesApplicationBackend

    override lateinit var window: EglGlesWindow
    override lateinit var renderSystem: RenderSystem

    override fun init() {
        this.window = Window.of(
            name = "",
            handler = EmptyWindowHandler,
            width = 720,
            height = 420
        ) as? EglGlesWindow ?: throw IllegalStateException("Window is not GlfwGlesWindow. Why?")

        this.renderSystem = GlesRenderSystem

        window.create()

        GLES.createCapabilities()
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
            return GlesApplication
        }
    }
}