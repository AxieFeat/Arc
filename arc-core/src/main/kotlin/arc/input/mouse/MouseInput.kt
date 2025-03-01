package arc.input.mouse

import arc.annotations.MutableType
import arc.input.DeviceType
import arc.input.InputDevice
import arc.math.Point2d

/**
 * This interface represents mouse input device.
 *
 * @see InputDevice
 */
@MutableType
@Suppress("INAPPLICABLE_JVM_NAME")
interface MouseInput : InputDevice {

    /**
     * Position of cursor in window.
     */
    @get:JvmName("position")
    var position: Point2d

    override val type: DeviceType
        get() = DeviceType.MOUSE

    override val name: String
        get() = "mouse"

}