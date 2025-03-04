package arc.input.keyboard

import arc.input.ArcBindingProcessor
import arc.input.BindingProcessor
import arc.input.KeyCode
import arc.input.KeyType
import arc.input.mouse.ArcMouseInput
import arc.window.Window
import org.lwjgl.glfw.GLFW

internal object ArcKeyboardInput : KeyboardInput {

    lateinit var window: Window
    override val bindingProcessor: BindingProcessor = ArcBindingProcessor()

    override fun isPressed(key: KeyCode): Boolean {
        if(key.keyType != KeyType.KEY) return false

        try {
            if(GLFW.glfwGetKey(ArcMouseInput.window.handle, key.id) == GLFW.GLFW_PRESS) return true
        } catch (throwable: Throwable) {
            return false
        }

        return false
    }

    override fun isReleased(key: KeyCode): Boolean {
        if(key.keyType != KeyType.KEY) return false

        try {
            if(GLFW.glfwGetKey(ArcMouseInput.window.handle, key.id) == GLFW.GLFW_RELEASE) return true
        } catch (throwable: Throwable) {
            return false
        }

        return false
    }

    fun keyUpdate(key: KeyCode, pressed: Boolean) {
        bindingProcessor.bindings.forEach { binding ->
            if(binding.key == key || binding.key == KeyCode.ANY || binding.key == KeyCode.ANY_KEY) {
                if(pressed) {
                    binding.onPress(key)
                } else {
                    binding.onRelease(key)
                }
            }
        }
    }

}