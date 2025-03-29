package arc.input

import arc.input.mouse.MouseInput
import arc.input.keyboard.KeyboardInput

/**
 * This interface represents some input device (e.g. Keyboard or Mouse).
 *
 * @see MouseInput
 * @see KeyboardInput
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface InputDevice {

    /**
     * Name of device.
     */
    @get:JvmName("name")
    val name: String

    /**
     * Type of device.
     */
    @get:JvmName("type")
    val type: DeviceType

    /**
     * Processor for bindings.
     */
    @get:JvmName("bindingProcessor")
    val bindingProcessor: BindingProcessor

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