package arc.input.mouse

import arc.input.DeviceType
import arc.input.InputDevice
import org.joml.Vector2f

/**
 * This interface represents mouse input device.
 *
 * @see InputDevice
 */
interface MouseInput : InputDevice {

    /**
     * Position of mouse in previous frame.
     */
    val previousPosition: Vector2f

    /**
     * Position of cursor in window.
     */
    var position: Vector2f

    /**
     * Vector that contains the difference between the old and new mouse position.
     */
    val displayVec: Vector2f

    override val type: DeviceType
        get() = DeviceType.MOUSE

    override val name: String
        get() = "mouse"

    /**
     * Reset [displayVec].
     */
    fun reset()

}