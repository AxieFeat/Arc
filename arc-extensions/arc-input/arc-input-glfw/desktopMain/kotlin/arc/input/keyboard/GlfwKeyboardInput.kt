package arc.input.keyboard

import arc.input.Binding
import arc.input.BindingProcessor
import arc.input.GlfwBindingProcessor
import arc.input.GlfwInputEngine
import arc.input.KeyCode
import arc.input.KeyType
import arc.input.MultiBinding
import arc.window.Window
import org.lwjgl.glfw.GLFW

internal object GlfwKeyboardInput : KeyboardInput {

    var window: Window? = null
    override val bindingProcessor: BindingProcessor = GlfwBindingProcessor()

    override fun isPressed(key: KeyCode): Boolean {
        if (key.keyType != KeyType.KEY) return false

        return window?.events?.gameplayKeyDown(key.id) ?: false
    }

    override fun isReleased(key: KeyCode): Boolean {
        if (key.keyType != KeyType.KEY) return false

        return !isPressed(key)
    }

    @Suppress("OptionalWhenBraces")
    fun keyUpdate(key: KeyCode, pressed: Boolean) {
        if (key.keyType != KeyType.KEY) return

        GlfwInputEngine.executeBinding {
            bindingProcessor.bindings.forEach { binding ->

                GlfwInputEngine.executeBinding {
                    when (binding) {
                        is Binding if (
                                binding.key == key ||
                                        binding.key == KeyCode.ANY ||
                                        binding.key == KeyCode.ANY_KEY
                                ) -> {

                            if (pressed) {
                                binding.onPress(key)
                            } else {
                                binding.onRelease(key)
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
