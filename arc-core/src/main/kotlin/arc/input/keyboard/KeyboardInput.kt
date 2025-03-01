package arc.input.keyboard

import arc.annotations.ImmutableType
import arc.input.DeviceType
import arc.input.InputDevice

/**
 * This interface represents controller for keyboard device.
 *
 * @see InputDevice
 */
@ImmutableType
interface KeyboardInput : InputDevice {

    override val type: DeviceType
        get() = DeviceType.KEYBOARD

    override val name: String
        get() = "keyboard"

}