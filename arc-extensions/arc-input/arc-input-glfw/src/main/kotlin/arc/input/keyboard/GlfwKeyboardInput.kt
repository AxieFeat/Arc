package arc.input.keyboard

import arc.input.*
import arc.window.Window
import org.lwjgl.glfw.GLFW

internal object GlfwKeyboardInput : KeyboardInput {

    lateinit var window: Window
    override val bindingProcessor: BindingProcessor = GlfwBindingProcessor()

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

        GlfwInputEngine.executor.submit {
            bindingProcessor.bindings.forEach { binding ->

                GlfwInputEngine.executor.submit {
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
                            if (GlfwInputEngine.checkForAll(binding.keys, pressed)) {
                                binding.onPress()
                            }
                        }
                    }
                }

            }
        }
    }

}