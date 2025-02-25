package arc.window

import arc.math.Point2i
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.GLFW_FALSE
import org.lwjgl.glfw.GLFW.GLFW_TRUE
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.MemoryUtil

internal class ArcWindow(
    override val name: String,
    override var handler: WindowHandler,
    override var width: Int,
    override var height: Int
) : Window {

    private val defaultErrorCallback = GLFWErrorCallback.create(ArcWindow::bootCrash)

    override var handle: Long = 0
    override val position: Point2i = Point2i.of(0, 0)
    override var isFocus: Boolean = true

    override var isResizable: Boolean = true
        set(value) {
            field = value
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, if(isResizable) GLFW_TRUE else GLFW_FALSE)
        }

    override var isHide: Boolean = false
        set(value) {
            field = value
            GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, if(value) GLFW_FALSE else GLFW_TRUE)
        }

    override fun resize(width: Int, height: Int) {
        GLFW.glfwSetWindowSize(handle, width, height)
    }

    override fun create() {
        GLFW.glfwSetErrorCallback(defaultErrorCallback)

        check(GLFW.glfwInit()) { "Unable to initialize GLFW" }

        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, if(isHide) GLFW_FALSE else GLFW_TRUE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, if(isResizable) GLFW_TRUE else GLFW_FALSE)

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1)

        handle = GLFW.glfwCreateWindow(width, height, name, MemoryUtil.NULL, MemoryUtil.NULL)

        GLFW.glfwSetWindowPosCallback(handle, ::onMove)
        GLFW.glfwSetFramebufferSizeCallback(handle, ::onResize)
        GLFW.glfwSetWindowFocusCallback(handle, ::onFocus)
        GLFW.glfwSetCursorEnterCallback(handle, ::onEnter)
        GLFW.glfwSetCursorPosCallback(handle, ::onCursorMove)
        GLFW.glfwSetScrollCallback(handle, ::onScroll)

        GLFW.glfwMakeContextCurrent(handle)

        val arrWidth = IntArray(1)
        val arrHeight = IntArray(1)
        GLFW.glfwGetFramebufferSize(handle, arrWidth, arrHeight)
        width = arrWidth[0]
        height = arrHeight[0]
    }

    override fun close() {
        Callbacks.glfwFreeCallbacks(handle)
        this.defaultErrorCallback.close()
        GLFW.glfwDestroyWindow(handle)
        GLFW.glfwTerminate()
    }

    private fun onMove(handle: Long, x: Int, y: Int) {
        this.position.x = x
        this.position.y = y

        handler.windowMove(x, y)
    }

    private fun onCursorMove(handle: Long, x: Double, y: Double) {
        handler.cursorMove(x, y)
    }

    private fun onScroll(handle: Long, xOffset: Double, yOffset: Double) {
        handler.scroll(xOffset, yOffset)
    }

    private fun onResize(handle: Long, width: Int, height: Int) {
        this.width = width
        this.height = height

        handler.resize(width, height)
    }

    private fun onFocus(handle: Long, focus: Boolean) {
        this.isFocus = focus

        handler.focus(focus)
    }

    private fun onEnter(handle: Long, entered: Boolean) {
        if(entered) handler.cursorEntered() else handler.cursorLeaved()
    }

    object Factory : Window.Factory {
        override fun create(name: String, handler: WindowHandler, width: Int, height: Int): Window {
            return ArcWindow(name, handler, width, height)
        }
    }

    companion object {
        private fun bootCrash(code: Int, text: Long) {
            throw WindowException("GLFW error $code: ${MemoryUtil.memUTF8(text)}")
        }
    }

}