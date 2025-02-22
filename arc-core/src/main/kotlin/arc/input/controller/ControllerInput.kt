package arc.input.controller

import arc.annotations.ImmutableType
import arc.input.DeviceType
import arc.input.InputDevice
import arc.input.KeyCode

/**
 * This interface represents controller input device.
 *
 * @see InputDevice
 */
@ImmutableType
@Suppress("INAPPLICABLE_JVM_NAME")
interface ControllerInput : InputDevice {

    /**
     * ID of this controller.
     *
     * Multiple controllers are supported, so each has its own ID.
     */
    @get:JvmName("id")
    val id: Int

    override val type: DeviceType
        get() = DeviceType.CONTROLLER

    override val name: String
        get() = "controller-$id"

    /**
     * An axis tilt value, usually -1 to 1; 0 for non-axes.
     *
     * @param key Key to check.
     */
    fun getAxis(key: KeyCode): Float

}