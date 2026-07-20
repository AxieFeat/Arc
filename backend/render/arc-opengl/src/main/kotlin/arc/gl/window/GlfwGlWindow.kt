package arc.gl.window

import arc.window.Window
import arc.window.WindowHandler
import arc.window.glfw.AbstractGlfwWindow
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.lwjgl.glfw.GLFW

internal class GlfwGlWindow(
    name: String,
    handler: WindowHandler,
    width: Int,
    height: Int,
    isResizable: Boolean
) : AbstractGlfwWindow(name, handler, width, height, isResizable) {

    override val logger: KLogger = KotlinLogging.logger {}

    init {
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1)
    }

    override fun create() {
        super.create()

        GLFW.glfwMakeContextCurrent(handle)
    }

    fun pollEvents() {
        GLFW.glfwPollEvents()
    }

    fun swapBuffers() {
        GLFW.glfwSwapBuffers(handle)
    }

    object Factory : Window.Factory {

        override fun create(name: String, handler: WindowHandler, width: Int, height: Int, isResizable: Boolean): Window {
            return GlfwGlWindow(name, handler, width, height, isResizable)
        }
    }
}
