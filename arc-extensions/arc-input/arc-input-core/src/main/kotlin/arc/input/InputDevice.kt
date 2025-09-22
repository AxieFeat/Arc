package arc.input

import arc.input.mouse.MouseInput
import arc.input.keyboard.KeyboardInput

/**
 * This interface represents some input device (e.g. Keyboard or Mouse).
 *
 * @see MouseInput
 * @see KeyboardInput
 */
interface InputDevice : BindingHolder {

    /**
     * Name of device.
     */
    val name: String

    /**
     * Type of device.
     */
    val type: DeviceType

    /**
     * Whether the button is currently pressed.
     *
     * @param key Key to check
     */
    fun isPressed(key: KeyCode): Boolean

    /**
     * Whether this button was released this frame.
     *
     * @param key Key to check.
     */
    fun isReleased(key: KeyCode): Boolean
}
