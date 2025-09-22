package arc.input

import arc.input.keyboard.GlfwKeyboardInput
import arc.input.keyboard.GlfwKeyboardInput.isPressed
import arc.input.keyboard.KeyboardInput
import arc.input.mouse.GlfwMouseInput
import arc.input.mouse.MouseInput
import arc.window.Window
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback
import org.lwjgl.glfw.GLFW.glfwSetKeyCallback
import org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback
import org.lwjgl.glfw.GLFW.glfwSetScrollCallback
import java.util.concurrent.Executors

@Suppress("unused")
object GlfwInputEngine : InputEngine {

    internal val executor = Executors.newFixedThreadPool(4)

    private var window: Window? = null

    override val mouse: MouseInput
        get() = GlfwMouseInput

    override  val keyboard: KeyboardInput
        get() = GlfwKeyboardInput

    /**
     * Hook `arc-input-glfw` to some window. You can hook only if window is initialized.
     *
     * @param window Window to hook
     */
    @JvmStatic
    fun hook(window: Window) {
        this.window = window

        GlfwMouseInput.window = window
        GlfwKeyboardInput.window = window

        val handle = window.handle

        glfwSetKeyCallback(handle, ::onKey)
        glfwSetMouseButtonCallback(handle, ::onMouseButton)
        glfwSetCursorPosCallback(handle, ::onCursorMove)
        glfwSetScrollCallback(handle, ::onScroll)
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
        if(!pressed) return false

        var status = true

        keys.forEach { key ->
            if(!isPressed(key)) {
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
