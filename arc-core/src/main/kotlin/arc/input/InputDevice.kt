package arc.input

import arc.annotations.ImmutableType
import arc.input.mouse.MouseInput
import arc.input.controller.ControllerInput
import arc.input.keyboard.KeyboardInput

/**
 * This interface represents some input device (e.g. Keyboard or Controller).
 *
 * @see MouseInput
 * @see ControllerInput
 * @see KeyboardInput
 */
@ImmutableType
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