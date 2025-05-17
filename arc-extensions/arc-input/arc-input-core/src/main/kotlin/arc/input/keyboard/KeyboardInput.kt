package arc.input.keyboard

import arc.input.DeviceType
import arc.input.InputDevice

/**
 * This interface represents keyboard input device.
 *
 * @see InputDevice
 */
interface KeyboardInput : InputDevice {

    override val type: DeviceType
        get() = DeviceType.KEYBOARD

    override val name: String
        get() = "keyboard"

}