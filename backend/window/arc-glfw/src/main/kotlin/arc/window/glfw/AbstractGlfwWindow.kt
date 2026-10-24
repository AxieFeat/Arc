package arc.window.glfw

import arc.window.CursorShape
import arc.window.FocusEvent
import arc.window.KeyAction
import arc.window.KeyModifiers
import arc.window.KeySymbol
import arc.window.KeyboardEvent
import arc.window.MetricsEvent
import arc.window.PointerBoundary
import arc.window.PointerButton
import arc.window.PointerMotion
import arc.window.PointerWheel
import arc.window.UnicodeCommit
import arc.window.Window
import arc.window.WindowBackend
import arc.window.WindowEvents
import arc.window.WindowException
import arc.window.WindowHandler
import arc.window.WindowMetrics
import io.github.oshai.kotlinlogging.KLogger
import org.joml.Vector2i
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.GLFW_CURSOR
import org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED
import org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL
import org.lwjgl.glfw.GLFW.GLFW_FALSE
import org.lwjgl.glfw.GLFW.GLFW_RESIZABLE
import org.lwjgl.glfw.GLFW.GLFW_TRUE
import org.lwjgl.glfw.GLFW.GLFW_VISIBLE
import org.lwjgl.glfw.GLFW.glfwCreateWindow
import org.lwjgl.glfw.GLFW.glfwDefaultWindowHints
import org.lwjgl.glfw.GLFW.glfwDestroyWindow
import org.lwjgl.glfw.GLFW.glfwGetClipboardString
import org.lwjgl.glfw.GLFW.glfwGetFramebufferSize
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.glfw.GLFW.glfwHideWindow
import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFW.glfwSetClipboardString
import org.lwjgl.glfw.GLFW.glfwSetCursorEnterCallback
import org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback
import org.lwjgl.glfw.GLFW.glfwSetErrorCallback
import org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback
import org.lwjgl.glfw.GLFW.glfwSetInputMode
import org.lwjgl.glfw.GLFW.glfwSetScrollCallback
import org.lwjgl.glfw.GLFW.glfwSetWindowFocusCallback
import org.lwjgl.glfw.GLFW.glfwSetWindowPosCallback
import org.lwjgl.glfw.GLFW.glfwSetWindowSize
import org.lwjgl.glfw.GLFW.glfwSetWindowTitle
import org.lwjgl.glfw.GLFW.glfwShowWindow
import org.lwjgl.glfw.GLFW.glfwSwapInterval
import org.lwjgl.glfw.GLFW.glfwTerminate
import org.lwjgl.glfw.GLFW.glfwWindowHint
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.MemoryUtil

@Suppress("UNUSED_PARAMETER")
abstract class AbstractGlfwWindow(
    name: String,
    override var handler: WindowHandler,
    override var width: Int,
    override var height: Int,
    override val isResizable: Boolean
) : Window {

    protected abstract val logger: KLogger

    private val defaultErrorCallback = GLFWErrorCallback.create(AbstractGlfwWindow::bootCrash)

    override val backend: WindowBackend = GlfwWindowBackend

    override var handle: Long = 0
    override var position: Vector2i = Vector2i(0, 0)
    override var isFocus: Boolean = true
    override val events = WindowEvents()

    init {
        // Compatibility input is a lower-priority listener, so UI consumption also protects
        // applications still using Window.handler for camera movement or scrolling.
        events.subscribe(priority = Int.MIN_VALUE) { event ->
            when (event) {
                is PointerMotion -> handler.cursorMove(event.x, event.y)
                is PointerWheel -> handler.scroll(event.deltaX, event.deltaY)
                is PointerBoundary -> if (event.entered) handler.cursorEntered() else handler.cursorLeaved()
            }
            false
        }
    }

    final override var metrics = WindowMetrics(width, height, width, height)
        private set
    private var cursorX = 0.0
    private var cursorY = 0.0
    private var closed = false
    private val cursors = mutableMapOf<CursorShape, Long>()
    override fun setCursor(shape: CursorShape) {
        require(handle != 0L && !closed)
        if (shape == CursorShape.DEFAULT) {
            GLFW.glfwSetCursor(handle, 0L)
            return
        }
        val cursor = cursors.getOrPut(shape) {
            GLFW.glfwCreateStandardCursor(
                when (shape) {
                    CursorShape.POINTER -> GLFW.GLFW_HAND_CURSOR
                    CursorShape.TEXT -> GLFW.GLFW_IBEAM_CURSOR
                    CursorShape.CROSSHAIR -> GLFW.GLFW_CROSSHAIR_CURSOR
                    CursorShape.RESIZE_HORIZONTAL -> GLFW.GLFW_HRESIZE_CURSOR
                    CursorShape.RESIZE_VERTICAL -> GLFW.GLFW_VRESIZE_CURSOR
                    else -> GLFW.GLFW_ARROW_CURSOR
                }
            )
        }
        check(cursor != 0L) { "Could not create cursor $shape" }
        GLFW.glfwSetCursor(handle, cursor)
    }

    override var clipboardText: String
        get() {
            return glfwGetClipboardString(handle) ?: ""
        }
        set(value) {
            // Maybe replace 0L to some constant?
            require(handle != 0L) { "Window is not initialized" }
            glfwSetClipboardString(handle, value)
        }

    override var name: String = name
        set(value) {
            field = value

            glfwSetWindowTitle(handle, value)
        }

    override val timeFromInitialize: Double
        get() = glfwGetTime()

    override var isHidden: Boolean = false
        set(value) {
            if (value && !field) {
                glfwHideWindow(handle)
            } else if (!value && field) {
                glfwShowWindow(handle)
            }

            field = value
        }

    override var isVsync: Boolean = true
        set(value) {
            if (value && !field) {
                glfwSwapInterval(GLFW_TRUE)
            } else if (!value && field) {
                glfwSwapInterval(GLFW_FALSE)
            }

            field = value
        }

    override var isShowCursor: Boolean = true
        set(value) {
            if (value && !field) {
                glfwSetInputMode(
                    handle,
                    GLFW_CURSOR,
                    GLFW_CURSOR_NORMAL
                )
            } else if (!value && field) {
                glfwSetInputMode(
                    handle,
                    GLFW_CURSOR,
                    GLFW_CURSOR_DISABLED
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
        GLFW.glfwSetWindowSizeCallback(handle) { _, _, _ -> refreshMetrics() }
        GLFW.glfwSetWindowContentScaleCallback(handle) { _, _, _ -> refreshMetrics() }
        GLFW.glfwSetWindowIconifyCallback(handle) { _, _ -> refreshMetrics() }
        GLFW.glfwSetKeyCallback(handle) { _, key, scan, action, mods ->
            events.enqueue(
                KeyboardEvent(symbol(key), key, scan, keyAction(action), modifiers(mods), events.nextSequence(), System.nanoTime(), handle)
            )
        }
        GLFW.glfwSetMouseButtonCallback(handle) { _, button, action, mods ->
            events.enqueue(
                PointerButton(button, keyAction(action), cursorX, cursorY, modifiers(mods), events.nextSequence(), System.nanoTime(), handle)
            )
        }
        GLFW.glfwSetCharCallback(handle) { _, codepoint ->
            events.enqueue(UnicodeCommit(String(Character.toChars(codepoint)), events.nextSequence(), System.nanoTime(), handle))
        }

        val arrWidth = IntArray(1)
        val arrHeight = IntArray(1)
        glfwGetFramebufferSize(handle, arrWidth, arrHeight)
        width = arrWidth[0]
        height = arrHeight[0]
        refreshMetrics()

        logger.atInfo {
            message = "Initialized GLFW window"
            payload = mapOf(
                "name" to name,
                "width" to width,
                "height" to height,
                "resizable" to isResizable,
                "isVsync" to isVsync
            )
        }
    }

    override fun close() {
        if (closed) return
        closed = true
        events.close()
        cursors.values.forEach(GLFW::glfwDestroyCursor)
        cursors.clear()
        logger.info { "Closing GLFW window" }

        Callbacks.glfwFreeCallbacks(handle)
        this.defaultErrorCallback.close()
        glfwDestroyWindow(handle)
        glfwTerminate()

        logger.debug { "Destroyed GLFW window $handle" }
        logger.info { "GLFW window has been closed" }
    }

    override fun shouldClose(): Boolean {
        return glfwWindowShouldClose(handle)
    }

    override fun resize(width: Int, height: Int) {
        glfwSetWindowSize(handle, width, height)
    }

    private fun onMove(handle: Long, x: Int, y: Int) {
        position.set(x, y)

        handler.windowMove(x, y)
    }

    private fun onCursorMove(handle: Long, x: Double, y: Double) {
        cursorX = x
        cursorY = y
        events.enqueue(PointerMotion(x, y, events.nextSequence(), System.nanoTime(), handle))
    }

    private fun onScroll(handle: Long, xOffset: Double, yOffset: Double) {
        events.enqueue(PointerWheel(cursorX, cursorY, xOffset, yOffset, events.nextSequence(), System.nanoTime(), handle))
    }

    private fun onResize(handle: Long, width: Int, height: Int) {
        this.width = width
        this.height = height
        refreshMetrics()

        handler.resize(width, height)
    }

    private fun onFocus(handle: Long, focus: Boolean) {
        this.isFocus = focus
        refreshMetrics()
        events.enqueue(FocusEvent(focus, events.nextSequence(), System.nanoTime(), handle))

        handler.focus(focus)
    }

    private fun onEnter(handle: Long, entered: Boolean) {
        events.enqueue(PointerBoundary(entered, events.nextSequence(), System.nanoTime(), handle))
    }

    private fun refreshMetrics() {
        val w = IntArray(1)
        val h = IntArray(1)
        val fw = IntArray(1)
        val fh = IntArray(1)
        val sx = FloatArray(1)
        val sy = FloatArray(1)
        GLFW.glfwGetWindowSize(handle, w, h)
        GLFW.glfwGetFramebufferSize(handle, fw, fh)
        GLFW.glfwGetWindowContentScale(handle, sx, sy)
        width = fw[0]
        height = fh[0]
        metrics = WindowMetrics(
            w[0], h[0], fw[0], fh[0], sx[0], sy[0], isFocus, GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE,
            metrics.generation + 1
        )
        events.enqueue(MetricsEvent(metrics, events.nextSequence(), System.nanoTime(), handle))
    }

    private fun keyAction(action: Int) = when (action) {
        GLFW.GLFW_PRESS -> KeyAction.PRESS
        GLFW.GLFW_REPEAT -> KeyAction.REPEAT
        else -> KeyAction.RELEASE
    }

    private fun modifiers(mods: Int) = KeyModifiers(
        mods and GLFW.GLFW_MOD_SHIFT != 0, mods and GLFW.GLFW_MOD_CONTROL != 0, mods and GLFW.GLFW_MOD_ALT != 0, mods and GLFW.GLFW_MOD_SUPER != 0
    )

    private fun symbol(key: Int) = when (key) {
        GLFW.GLFW_KEY_TAB -> KeySymbol.TAB
        GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> KeySymbol.ENTER
        GLFW.GLFW_KEY_SPACE -> KeySymbol.SPACE
        GLFW.GLFW_KEY_ESCAPE -> KeySymbol.ESCAPE
        GLFW.GLFW_KEY_LEFT -> KeySymbol.LEFT
        GLFW.GLFW_KEY_RIGHT -> KeySymbol.RIGHT
        GLFW.GLFW_KEY_UP -> KeySymbol.UP
        GLFW.GLFW_KEY_DOWN -> KeySymbol.DOWN
        GLFW.GLFW_KEY_HOME -> KeySymbol.HOME
        GLFW.GLFW_KEY_END -> KeySymbol.END
        GLFW.GLFW_KEY_BACKSPACE -> KeySymbol.BACKSPACE
        GLFW.GLFW_KEY_DELETE -> KeySymbol.DELETE
        GLFW.GLFW_KEY_A -> KeySymbol.A
        GLFW.GLFW_KEY_C -> KeySymbol.C
        GLFW.GLFW_KEY_V -> KeySymbol.V
        GLFW.GLFW_KEY_X -> KeySymbol.X
        GLFW.GLFW_KEY_Y -> KeySymbol.Y
        GLFW.GLFW_KEY_Z -> KeySymbol.Z
        else -> KeySymbol.UNKNOWN
    }

    companion object {

        @JvmStatic
        private fun bootCrash(code: Int, text: Long) {
            throw WindowException("GLFW error $code: ${MemoryUtil.memUTF8(text)}")
        }
    }
}
