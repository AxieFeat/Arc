package arc.input

import arc.input.keyboard.GlfwKeyboardInput
import arc.input.keyboard.KeyboardInput
import arc.input.mouse.GlfwMouseInput
import arc.input.mouse.MouseInput
import arc.window.KeyAction
import arc.window.KeyboardEvent
import arc.window.PointerButton
import arc.window.PointerMotion
import arc.window.PointerWheel
import arc.window.Window
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.glfwGetKey
import org.lwjgl.glfw.GLFW.glfwGetMouseButton
import org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback
import org.lwjgl.glfw.GLFW.glfwSetKeyCallback
import org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback
import org.lwjgl.glfw.GLFW.glfwSetScrollCallback
import java.util.concurrent.Executors

@Suppress("unused")
object GlfwInputEngine : InputEngine {

    private val executor by lazy { Executors.newFixedThreadPool(4) { r -> Thread(r, "arc-input-legacy").apply { isDaemon = true } } }
    var legacyAsyncBindings: Boolean = false

    internal fun executeBinding(action: () -> Unit) {
        if (legacyAsyncBindings) executor.submit(action) else action()
    }

    private var subscription: AutoCloseable? = null

    private var window: Window? = null

    override val mouse: MouseInput
        get() = GlfwMouseInput

    override val keyboard: KeyboardInput
        get() = GlfwKeyboardInput

    override val physicalKeyboard: KeyboardInput = object : KeyboardInput by GlfwKeyboardInput {
        override fun isPressed(key: KeyCode) = window?.let { glfwGetKey(it.handle, key.id) == GLFW_PRESS } ?: false

        override fun isReleased(key: KeyCode) = !isPressed(key)
    }
    override val physicalMouse: MouseInput = object : MouseInput by GlfwMouseInput {
        override fun isPressed(key: KeyCode) = window?.let { glfwGetMouseButton(it.handle, key.id) == GLFW_PRESS } ?: false

        override fun isReleased(key: KeyCode) = !isPressed(key)
    }

    /**
     * Hook `arc-input-glfw` to some window. You can hook only if window is initialized.
     *
     * @param window Window to hook
     */
    @JvmStatic
    fun hook(window: Window) {
        subscription?.close()
        this.window = window

        GlfwMouseInput.window = window
        GlfwKeyboardInput.window = window

        subscription = window.events.subscribe(priority = -100) { event ->
            when (event) {
                is KeyboardEvent -> if (event.action != KeyAction.REPEAT) GlfwKeyboardInput.keyUpdate(
                    KeyCode.fromId(event.physicalCode), event.action == KeyAction.PRESS
                )

                is PointerButton -> if (event.action != KeyAction.REPEAT) GlfwMouseInput.keyUpdate(
                    KeyCode.fromId(event.button), event.action == KeyAction.PRESS
                )

                is PointerMotion -> GlfwMouseInput.positionUpdate(event.x, event.y)
                is PointerWheel -> GlfwMouseInput.scrollUpdate(event.deltaX, event.deltaY)
            }
            false
        }
    }

    @JvmStatic
    fun unhook() {
        subscription?.close()
        subscription = null
        window = null
        GlfwKeyboardInput.window = null
        GlfwMouseInput.window = null
    }

    private fun onKey(handle: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        GlfwKeyboardInput.keyUpdate(KeyCode.fromId(key), action == GLFW_PRESS)
    }

    private fun onMouseButton(handle: Long, button: Int, action: Int, mods: Int) {
        GlfwMouseInput.keyUpdate(KeyCode.fromId(button), action == GLFW_PRESS)
    }

    private fun onCursorMove(handle: Long, x: Double, y: Double) {
        window?.handler?.cursorMove(x, y)

        GlfwMouseInput.positionUpdate(x, y)
    }

    private fun onScroll(handle: Long, xOffset: Double, yOffset: Double) {
        window?.handler?.scroll(xOffset, yOffset)

        GlfwMouseInput.scrollUpdate(xOffset, yOffset)
    }

    @JvmStatic
    internal fun checkForAll(keys: Set<KeyCode>, pressed: Boolean): Boolean {
        if (!pressed) return false

        var status = true

        keys.forEach { key ->
            if (!keyboard.isPressed(key) && !mouse.isPressed(key)) {
                status = false
            }
        }

        return status
    }

    object Provider : InputEngine.Provider {

        override fun provide(): InputEngine {
            return GlfwInputEngine
        }
    }
}
