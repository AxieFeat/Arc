package arc.model

import arc.annotations.ImmutableType
import arc.math.Point3d
import arc.model.cube.Cube
import arc.util.pattern.Identifiable

/**
 * Represents a structural or functional component in 3D space.
 *
 * @see Cube
 * @see Model
 */
@ImmutableType
interface Element : Identifiable {

    /**
     * The origin point of the 3D element in space.
     */
    val origin: Point3d

}