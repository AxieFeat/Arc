package arc.gl.window

import arc.window.AbstractGlfwWindow
import arc.window.Window
import arc.window.WindowHandler
import org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR
import org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR
import org.lwjgl.glfw.GLFW.glfwMakeContextCurrent
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.glfw.GLFW.glfwSwapBuffers
import org.lwjgl.glfw.GLFW.glfwWindowHint

internal class GlfwGlWindow(
    name: String,
    handler: WindowHandler,
    width: Int,
    height: Int,
    isResizable: Boolean
) : AbstractGlfwWindow(name, handler, width, height, isResizable) {

    init {
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1)
    }

    override fun create() {
        super.create()

        glfwMakeContextCurrent(handle)
    }

    fun pollEvents() {
        glfwPollEvents()
    }

    fun swapBuffers() {
        glfwSwapBuffers(handle)
    }

    object Factory : Window.Factory {
        override fun create(name: String, handler: WindowHandler, width: Int, height: Int, isResizable: Boolean): Window {
            return GlfwGlWindow(name, handler, width, height, isResizable)
        }
    }

}