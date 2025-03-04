package arc.input.mouse

import arc.input.*
import arc.input.ArcBindingProcessor
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

            when(binding) {
                is Binding -> {
                    if (binding.key == key || binding.key == KeyCode.ANY || binding.key == KeyCode.ANY_MOUSE) {
                        if (pressed) {
                            binding.onPress(key)
                        } else {
                            binding.onRelease(key)
                        }
                    }
                }

                is MultiBinding -> {
                    if(ArcInput.checkForAll(binding.keys, pressed)) {
                        binding.onPress()
                    }
                }
            }

        }
    }

    fun scrollUpdate(xOffset: Double, yOffset: Double) {
        bindingProcessor.bindings.forEach { binding ->

            when(binding) {
                is MouseBinding -> {
                    if (binding.key == KeyCode.MOUSE_SCROLL || binding.key == KeyCode.ANY || binding.key == KeyCode.ANY_MOUSE) {
                        binding.onScroll(xOffset, yOffset)
                    }
                }
            }

        }
    }


}