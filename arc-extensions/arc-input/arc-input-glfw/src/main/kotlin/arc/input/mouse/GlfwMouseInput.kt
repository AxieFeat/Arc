package arc.input.mouse

import arc.input.Binding
import arc.input.BindingProcessor
import arc.input.GlfwBindingProcessor
import arc.input.GlfwInputEngine
import arc.input.KeyCode
import arc.input.KeyType
import arc.input.MultiBinding
import arc.window.Window
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW

internal object GlfwMouseInput : MouseInput {

    var window: Window? = null
    override val bindingProcessor: BindingProcessor = GlfwBindingProcessor()
    override var previousPosition: Vector2f = Vector2f()
    override var position: Vector2f = Vector2f()
    override var displayVec: Vector2f = Vector2f()

    override fun isPressed(key: KeyCode): Boolean {
        if (key.keyType != KeyType.MOUSE) return false

        return window?.events?.gameplayButtonDown(key.id) ?: false
    }

    override fun isReleased(key: KeyCode): Boolean {
        if (key.keyType != KeyType.MOUSE) return false

        return !isPressed(key)
    }

    override fun reset() {
        displayVec.zero()
    }

    fun positionUpdate(x: Double, y: Double) {
        if (position.x == x.toFloat() && position.y == y.toFloat()) return

        previousPosition = previousPosition.set(position.x, position.y)

        position = position.set(x, y)

        displayVec = displayVec.set(
            position.x - previousPosition.x,
            position.y - previousPosition.y
        )
    }

    @Suppress("OptionalWhenBraces")
    fun keyUpdate(key: KeyCode, pressed: Boolean) {
        if (key == KeyCode.MOUSE_SCROLL) return

        GlfwInputEngine.executeBinding {
            bindingProcessor.bindings.forEach { binding ->

                GlfwInputEngine.executeBinding {
                    when (binding) {
                        is Binding if (
                                binding.key == key ||
                                        binding.key == KeyCode.ANY ||
                                        binding.key == KeyCode.ANY_MOUSE
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

    @Suppress("OptionalWhenBraces")
    fun scrollUpdate(xOffset: Double, yOffset: Double) {
        GlfwInputEngine.executeBinding {
            bindingProcessor.bindings.forEach { binding ->

                GlfwInputEngine.executeBinding {
                    when (binding) {
                        is MouseBinding if (
                                binding.key == KeyCode.MOUSE_SCROLL ||
                                        binding.key == KeyCode.ANY ||
                                        binding.key == KeyCode.ANY_MOUSE
                                ) -> {

                            binding.onScroll(xOffset, yOffset)
                        }
                    }
                }
            }
        }
    }
}
