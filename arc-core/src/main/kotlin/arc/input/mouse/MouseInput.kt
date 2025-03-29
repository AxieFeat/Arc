package arc.input.mouse

import arc.input.DeviceType
import arc.input.InputDevice
import arc.math.Point2d
import arc.math.Vec2f

/**
 * This interface represents mouse input device.
 *
 * @see InputDevice
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface MouseInput : InputDevice {

    /**
     * Position of mouse in previous frame.
     */
    @get:JvmName("previousPosition")
    val previousPosition: Point2d

    /**
     * Position of cursor in window.
     */
    @get:JvmName("position")
    var position: Point2d

    /**
     * Vector that contains the difference between the old and new mouse position.
     */
    @get:JvmName("displayVec")
    val displayVec: Vec2f

    override val type: DeviceType
        get() = DeviceType.MOUSE

    override val name: String
        get() = "mouse"

}