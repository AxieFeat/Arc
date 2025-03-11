package arc.input.mouse

import arc.input.*
import arc.math.Point2d
import arc.math.Vec2f
import arc.window.Window
import org.lwjgl.glfw.GLFW

internal object ArcMouseInput : MouseInput {

    lateinit var window: Window
    override val bindingProcessor: BindingProcessor = ArcBindingProcessor()
    override var previousPosition: Point2d = Point2d.ZERO
    override var position: Point2d = Point2d.ZERO
    override var displayVec: Vec2f = Vec2f.ZERO

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

    fun positionUpdate(x: Double, y: Double) {
        previousPosition = Point2d.of(position.x, position.y)

        position.x = x
        position.y = y

        displayVec.y = (position.x - previousPosition.x).toFloat()
        displayVec.x = (position.y - previousPosition.y).toFloat()
    }

    fun keyUpdate(key: KeyCode, pressed: Boolean) {
        if(key == KeyCode.MOUSE_SCROLL) return

        ArcInput.executor.submit {
            bindingProcessor.bindings.forEach { binding ->

                ArcInput.executor.submit {
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
                            if (ArcInput.checkForAll(binding.keys, pressed)) {
                                binding.onPress()
                            }
                        }
                    }
                }

            }
        }
    }

    fun scrollUpdate(xOffset: Double, yOffset: Double) {
        ArcInput.executor.submit {
            bindingProcessor.bindings.forEach { binding ->

                ArcInput.executor.submit {
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