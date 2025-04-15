package arc.model

import arc.math.Point3d
import arc.model.cube.Cube
import arc.util.pattern.Identifiable

/**
 * Represents a structural or functional component in 3D space.
 *
 * @see Cube
 * @see Model
 */
interface Element : Identifiable {

    /**
     * The origin point of the 3D element in space.
     */
    val origin: Point3d

    /**
     * The light emission level of an element.
     */
    val lightLevel: Byte

    /**
     * The light color of this element in model.
     */
    val lightColor: Int


}