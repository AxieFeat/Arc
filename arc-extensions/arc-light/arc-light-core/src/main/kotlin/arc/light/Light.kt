package arc.light

import arc.math.Vec3f
import arc.util.Color

/**
 * This interface represents
 */
interface Light {

    /**
     * Position of this light.
     */
    val position: Vec3f

    /**
     * Color of this light.
     */
    val color: Color

    /**
     * Emission of thus light.
     */
    val emission: Float

}