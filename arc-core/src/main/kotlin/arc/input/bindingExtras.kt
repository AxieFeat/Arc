@file:JvmSynthetic
package arc.input

import arc.input.mouse.MouseBinding
import arc.input.mouse.MouseInput

/**
 * Just bind some binding to input device.
 *
 * @param binding Binding for bind.
 */
@JvmSynthetic
fun InputDevice.bind(binding: BindingLike) = bindingProcessor.bind(binding)

/**
 * Just unbind some binding to input device.
 *
 * @param binding Binding for unbind.
 */
@JvmSynthetic
fun InputDevice.unbind(binding: BindingLike) = bindingProcessor.unbind(binding)

/**
 * Just unbind some binding to input device.
 *
 * @param id ID of binding for unbind.
 */
@JvmSynthetic
fun InputDevice.unbind(id: String) = bindingProcessor.unbind(id)

/**
 * Execute some action on pressing keycode.
 *
 * @param keyCode Key code for action.
 * @param action Action to execute.
 */
@JvmSynthetic
inline fun InputDevice.onPress(keyCode: KeyCode, crossinline action: (KeyCode) -> Unit) {
    bindingProcessor.onPress(keyCode, action)
}

/**
 * Execute some action on releasing keycode.
 *
 * @param keyCode Key code for action.
 * @param action Action to execute.
 */
@JvmSynthetic
inline fun InputDevice.onRelease(keyCode: KeyCode, crossinline action: (KeyCode) -> Unit) {
    bindingProcessor.onRelease(keyCode, action)
}

/**
 * Execute some action on mouse scrolling.
 *
 * @param action Action to execute.
 */
@JvmSynthetic
inline fun MouseInput.onScroll(crossinline action: (Double, Double) -> Unit) {
    bindingProcessor.onScroll(action)
}

/**
 * Execute some action on pressing keycode.
 *
 * @param keyCode Key code for action.
 * @param action Action to execute.
 */
@JvmSynthetic
inline fun BindingProcessor.onPress(keyCode: KeyCode, crossinline action: (KeyCode) -> Unit) {
    this.bind(
        object : Binding {
            override val id: String = "inline.onPress.${keyCode.name}.${this.hashCode()}"
            override val key: KeyCode = keyCode

            override fun onPress(key: KeyCode) {
                action.invoke(key)
            }
        }
    )
}

/**
 * Execute some action on releasing keycode.
 *
 * @param keyCode Key code for action.
 * @param action Action to execute.
 */
@JvmSynthetic
inline fun BindingProcessor.onRelease(keyCode: KeyCode, crossinline action: (KeyCode) -> Unit) {
    this.bind(
        object : Binding {
            override val id: String = "inline.onRelease.${keyCode.name}.${this.hashCode()}"
            override val key: KeyCode = keyCode

            override fun onRelease(key: KeyCode) {
                action.invoke(key)
            }
        }
    )
}

/**
 * Execute some action on mouse scrolling.
 *
 * @param action Action to execute.
 */
@JvmSynthetic
inline fun BindingProcessor.onScroll(crossinline action: (Double, Double) -> Unit) {
    this.bind(
        object : MouseBinding {
            override val id: String = "inline.onScroll.${this.hashCode()}}"
            override val key: KeyCode = KeyCode.MOUSE_SCROLL

            override fun onScroll(xOffset: Double, yOffset: Double) {
                action.invoke(xOffset, yOffset)
            }
        }
    )
}