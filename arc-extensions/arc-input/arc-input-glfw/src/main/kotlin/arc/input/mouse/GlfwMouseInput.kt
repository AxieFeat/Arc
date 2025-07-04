package arc.input.mouse

import arc.input.*
import arc.window.Window
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW

internal object GlfwMouseInput : MouseInput {

    lateinit var window: Window
    override val bindingProcessor: BindingProcessor = GlfwBindingProcessor()
    override var previousPosition: Vector2f = Vector2f()
    override var position: Vector2f = Vector2f()
    override var displayVec: Vector2f = Vector2f()

    override fun isPressed(key: KeyCode): Boolean {
        if(key.keyType != KeyType.MOUSE) return false

        try {
            if(GLFW.glfwGetMouseButton(window.handle, key.id) == GLFW.GLFW_PRESS ||
                GLFW.glfwGetMouseButton(window.handle, key.id) == GLFW.GLFW_REPEAT) return true
        } catch (throwable: Throwable) {
            return false
        }

        return false
    }

    override fun isReleased(key: KeyCode): Boolean {
        if(key.keyType != KeyType.MOUSE) return false

        try {
            if(GLFW.glfwGetMouseButton(window.handle, key.id) == GLFW.GLFW_RELEASE ||
                GLFW.glfwGetMouseButton(window.handle, key.id) == GLFW.GLFW_REPEAT) return true
        } catch (throwable: Throwable) {
            return false
        }

        return false
    }

    override fun reset() {
        displayVec.zero()
    }

    fun positionUpdate(x: Double, y: Double) {
        previousPosition.set(position.x, position.y)

        position.set(x, y)

        displayVec.set(
            position.x - previousPosition.x,
            position.y - previousPosition.y
        )
    }

    fun keyUpdate(key: KeyCode, pressed: Boolean) {
        if(key == KeyCode.MOUSE_SCROLL) return

        GlfwInputEngine.executor.submit {
            bindingProcessor.bindings.forEach { binding ->

                GlfwInputEngine.executor.submit {
                    when (binding) {
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
                            if (GlfwInputEngine.checkForAll(binding.keys, pressed)) {
                                binding.onPress()
                            }
                        }
                    }
                }

            }
        }
    }

    fun scrollUpdate(xOffset: Double, yOffset: Double) {
        GlfwInputEngine.executor.submit {
            bindingProcessor.bindings.forEach { binding ->

                GlfwInputEngine.executor.submit {
                    when (binding) {
                        is MouseBinding -> {
                            if (binding.key == KeyCode.MOUSE_SCROLL || binding.key == KeyCode.ANY || binding.key == KeyCode.ANY_MOUSE) {
                                binding.onScroll(xOffset, yOffset)
                            }
                        }
                    }
                }

            }
        }
    }


}