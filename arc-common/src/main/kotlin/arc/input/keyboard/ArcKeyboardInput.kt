package arc.input.keyboard

import arc.input.*
import arc.input.ArcBindingProcessor
import arc.input.mouse.ArcMouseInput
import arc.window.Window
import org.lwjgl.glfw.GLFW
import java.util.concurrent.Executors

internal object ArcKeyboardInput : KeyboardInput {

    lateinit var window: Window
    override val bindingProcessor: BindingProcessor = ArcBindingProcessor()

    override fun isPressed(key: KeyCode): Boolean {
        if(key.keyType != KeyType.KEY) return false

        try {
            if(GLFW.glfwGetKey(window.handle, key.id) == GLFW.GLFW_PRESS ||
                GLFW.glfwGetKey(window.handle, key.id) == GLFW.GLFW_REPEAT) return true
        } catch (ignore: Throwable) {}

        return false
    }

    override fun isReleased(key: KeyCode): Boolean {
        if(key.keyType != KeyType.KEY) return false

        try {
            if(GLFW.glfwGetKey(window.handle, key.id) == GLFW.GLFW_RELEASE ||
                GLFW.glfwGetKey(window.handle, key.id) == GLFW.GLFW_REPEAT) return true
        } catch (ignore: Throwable) {}

        return false
    }

    fun keyUpdate(key: KeyCode, pressed: Boolean) {
        if(key.keyType != KeyType.KEY) return

        ArcInput.executor.submit {
            bindingProcessor.bindings.forEach { binding ->

                ArcInput.executor.submit {
                    when (binding) {
                        is Binding -> {
                            if (binding.key == key || binding.key == KeyCode.ANY || binding.key == KeyCode.ANY_KEY) {
                                if (pressed) {
                                    binding.onPress(key)
                                } else {
                                    binding.onRelease(key)
                                }
                            }
                        }

                        is MultiBinding -> {
                            if (ArcInput.checkForAll(binding.keys, pressed)) {
                                binding.onPress()
                            }
                        }
                    }
                }

            }
        }
    }

}