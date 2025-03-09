package arc.window

import arc.input.KeyCode
import arc.input.KeyType
import arc.input.keyboard.ArcKeyboardInput
import arc.input.mouse.ArcMouseInput
import arc.math.Point2i
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.MemoryUtil

@Suppress("UNUSED_PARAMETER")
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
            glfwWindowHint(GLFW_RESIZABLE, if(isResizable) GLFW_TRUE else GLFW_FALSE)
        }

    override var isHide: Boolean = false
        set(value) {
            field = value
            glfwWindowHint(GLFW_VISIBLE, if(value) GLFW_FALSE else GLFW_TRUE)
        }

    override fun resize(width: Int, height: Int) {
        glfwSetWindowSize(handle, width, height)
    }

    override fun create() {
        glfwSetErrorCallback(defaultErrorCallback)

        check(glfwInit()) { "Unable to initialize GLFW" }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, if(isHide) GLFW_FALSE else GLFW_TRUE)
        glfwWindowHint(GLFW_RESIZABLE, if(isResizable) GLFW_TRUE else GLFW_FALSE)

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1)

        handle = glfwCreateWindow(width, height, name, MemoryUtil.NULL, MemoryUtil.NULL)

        glfwSetWindowPosCallback(handle, ::onMove)
        glfwSetFramebufferSizeCallback(handle, ::onResize)
        glfwSetWindowFocusCallback(handle, ::onFocus)
        glfwSetCursorEnterCallback(handle, ::onEnter)
        glfwSetCursorPosCallback(handle, ::onCursorMove)
        glfwSetScrollCallback(handle, ::onScroll)
        glfwSetKeyCallback(handle, ::onKey)
        glfwSetMouseButtonCallback(handle, ::onMouseButton)

        glfwMakeContextCurrent(handle)

        val arrWidth = IntArray(1)
        val arrHeight = IntArray(1)
        glfwGetFramebufferSize(handle, arrWidth, arrHeight)
        width = arrWidth[0]
        height = arrHeight[0]

        glfwSwapInterval(0)
    }

    override fun close() {
        Callbacks.glfwFreeCallbacks(handle)
        this.defaultErrorCallback.close()
        glfwDestroyWindow(handle)
        glfwTerminate()
    }

    override fun shouldClose(): Boolean {
        return glfwWindowShouldClose(handle)
    }

    override fun beginFrame() {
        glfwPollEvents()
    }

    override fun endFrame() {
        glfwSwapBuffers(handle)
    }

    private fun onKey(handle: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        ArcKeyboardInput.keyUpdate(KeyCode.fromId(key), action == GLFW_PRESS)
    }

    private fun onMouseButton(handle: Long, button: Int, action: Int, mods: Int) {
        ArcMouseInput.keyUpdate(KeyCode.fromId(button), action == GLFW_PRESS)
    }

    private fun onMove(handle: Long, x: Int, y: Int) {
        this.position.x = x
        this.position.y = y

        handler.windowMove(x, y)
    }

    private fun onCursorMove(handle: Long, x: Double, y: Double) {
        handler.cursorMove(x, y)

        ArcMouseInput.position.x = x
        ArcMouseInput.position.y = y
    }

    private fun onScroll(handle: Long, xOffset: Double, yOffset: Double) {
        handler.scroll(xOffset, yOffset)

        ArcMouseInput.scrollUpdate(xOffset, yOffset)
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