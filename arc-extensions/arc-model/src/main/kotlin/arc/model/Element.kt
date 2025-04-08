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
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Element : Identifiable {

    /**
     * The origin point of the 3D element in space.
     */
    @get:JvmName("origin")
    val origin: Point3d

}