package arc.input.mouse

import arc.input.DeviceType
import arc.input.InputDevice
import korlibs.math.geom.Vector2F

/**
 * This interface represents mouse input device.
 *
 * @see InputDevice
 */
interface MouseInput : InputDevice {

    /**
     * Position of mouse in previous frame.
     */
    val previousPosition: Vector2F

    /**
     * Position of cursor in window.
     */
    var position: Vector2F

    /**
     * Vector that contains the difference between the old and new mouse position.
     */
    val displayVec: Vector2F

    override val type: DeviceType
        get() = DeviceType.MOUSE

    override val name: String
        get() = "mouse"

    /**
     * Reset [displayVec].
     */
    fun reset()
}
