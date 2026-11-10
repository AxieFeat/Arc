package arc.input.mouse

import arc.input.Binding
import arc.input.BindingProcessor
import arc.input.GlfwBindingProcessor
import arc.input.GlfwInputEngine
import arc.input.KeyCode
import arc.input.KeyType
import arc.input.MultiBinding
import arc.window.Window
import korlibs.math.geom.Vector2F
import org.lwjgl.glfw.GLFW

internal object GlfwMouseInput : MouseInput {

    var window: Window? = null
    override val bindingProcessor: BindingProcessor = GlfwBindingProcessor()
    override var previousPosition: Vector2F = Vector2F(0f, 0f)
    override var position: Vector2F = Vector2F(0f, 0f)
    override var displayVec: Vector2F = Vector2F(0f, 0f)

    override fun isPressed(key: KeyCode): Boolean {
        if (key.keyType != KeyType.MOUSE) return false

        return window?.events?.gameplayButtonDown(key.id) ?: false
    }

    override fun isReleased(key: KeyCode): Boolean {
        if (key.keyType != KeyType.MOUSE) return false

        return !isPressed(key)
    }

    override fun reset() {
        displayVec = Vector2F(0f, 0f)
    }

    fun positionUpdate(x: Double, y: Double) {
        if (position.x == x.toFloat() && position.y == y.toFloat()) return

        previousPosition = position

        position = Vector2F(x.toFloat(), y.toFloat())

        displayVec = Vector2F(
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
