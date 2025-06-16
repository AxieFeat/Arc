package arc.window

import arc.math.Point2i
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.MemoryUtil

@Suppress("UNUSED_PARAMETER", "unused")
abstract class AbstractGlfwWindow(
    name: String,
    override var handler: WindowHandler,
    override var width: Int,
    override var height: Int,
    override val isResizable: Boolean
) : Window {

    private val defaultErrorCallback = GLFWErrorCallback.create(AbstractGlfwWindow::bootCrash)

    override val backend: WindowBackend = GlfwWindowBackend

    override var handle: Long = 0
    override var position: Point2i = Point2i.of(0, 0)
    override var isFocus: Boolean = true

    override var name: String = name
        set(value) {
            field = value

            glfwSetWindowTitle(handle, value)
        }

    override val timeFromInitialize: Double
        get() = glfwGetTime()

    override var isHidden: Boolean = false
        set(value) {
            if(value && !field) {
                glfwShowWindow(handle)
            } else if (!value && field) {
                glfwHideWindow(handle)
            }

            field = value
        }

    override var isVsync: Boolean = true
        set(value) {
            if(value && !field) {
                glfwSwapInterval(GLFW_FALSE)
            } else if(!value && field) {
                glfwSwapInterval(GLFW_TRUE)
            }

            field = value
        }

    override var isShowCursor: Boolean = true
        set(value) {
            if(value && !field) {
                glfwSetInputMode(
                    handle,
                    GLFW_CURSOR,
                    GLFW_CURSOR_DISABLED
                )
            } else if(!value && field) {
                glfwSetInputMode(
                    handle,
                    GLFW_CURSOR,
                    GLFW_CURSOR_NORMAL
                )
            }

            field = value
        }

    init {
        glfwSetErrorCallback(defaultErrorCallback)

        check(glfwInit()) { "Unable to initialize GLFW" }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, if (isHidden) GLFW_FALSE else GLFW_TRUE)
        glfwWindowHint(GLFW_RESIZABLE, if (isResizable) GLFW_TRUE else GLFW_FALSE)
    }

    override fun create() {
        handle = glfwCreateWindow(width, height, name, MemoryUtil.NULL, MemoryUtil.NULL)

        glfwSetWindowPosCallback(handle, ::onMove)
        glfwSetFramebufferSizeCallback(handle, ::onResize)
        glfwSetWindowFocusCallback(handle, ::onFocus)
        glfwSetCursorEnterCallback(handle, ::onEnter)
        glfwSetCursorPosCallback(handle, ::onCursorMove)
        glfwSetScrollCallback(handle, ::onScroll)

        val arrWidth = IntArray(1)
        val arrHeight = IntArray(1)
        glfwGetFramebufferSize(handle, arrWidth, arrHeight)
        width = arrWidth[0]
        height = arrHeight[0]
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

    override fun resize(width: Int, height: Int) {
        glfwSetWindowSize(handle, width, height)
    }

    private fun onMove(handle: Long, x: Int, y: Int) {
        this.position = position.withXY(x, y)

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
        if (entered) handler.cursorEntered() else handler.cursorLeaved()
    }

    companion object {
        private fun bootCrash(code: Int, text: Long) {
            throw WindowException("GLFW error $code: ${MemoryUtil.memUTF8(text)}")
        }
    }

}