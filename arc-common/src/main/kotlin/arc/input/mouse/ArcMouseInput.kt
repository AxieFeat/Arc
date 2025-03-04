package arc.input.mouse

import arc.input.ArcBindingProcessor
import arc.input.BindingProcessor
import arc.input.KeyCode
import arc.input.KeyType
import arc.math.Point2d
import arc.window.Window
import org.lwjgl.glfw.GLFW

internal object ArcMouseInput : MouseInput {

    lateinit var window: Window
    override val bindingProcessor: BindingProcessor = ArcBindingProcessor()
    override var position: Point2d = Point2d.ZERO

    override fun isPressed(key: KeyCode): Boolean {
        if(key.keyType != KeyType.MOUSE) return false

        try {
            if(GLFW.glfwGetKey(window.handle, key.id) == GLFW.GLFW_PRESS) return true
        } catch (throwable: Throwable) {
            return false
        }

        return false
    }

    override fun isReleased(key: KeyCode): Boolean {
        if(key.keyType != KeyType.MOUSE) return false

        try {
            if(GLFW.glfwGetKey(window.handle, key.id) == GLFW.GLFW_RELEASE) return true
        } catch (throwable: Throwable) {
            return false
        }

        return false
    }

    fun keyUpdate(key: KeyCode, pressed: Boolean) {
        if(key == KeyCode.MOUSE_SCROLL) return

        bindingProcessor.bindings.forEach { binding ->
            if(binding.key == key || binding.key == KeyCode.ANY || binding.key == KeyCode.ANY_MOUSE) {
                if(pressed) {
                    binding.onPress(key)
                } else {
                    binding.onRelease(key)
                }
            }
        }
    }

    fun scrollUpdate(xOffset: Double, yOffset: Double) {
        bindingProcessor.bindings.forEach { binding ->
            if(binding.key == KeyCode.MOUSE_SCROLL || binding.key == KeyCode.ANY || binding.key == KeyCode.ANY_MOUSE) {
                if(binding is MouseBinding) {
                    binding.onScroll(xOffset, yOffset)
                }
            }
        }
    }


}