package arc.graphics.g3d.model

import arc.annotations.ImmutableType
import arc.math.Point3d
import arc.graphics.g3d.model.cube.Cube
import arc.light.Lighting

/**
 * Represents a structural or functional component of a 3D model.
 *
 * An `Element` provides essential characteristics such as its lighting properties
 * and its origin position in a 3D space. These components are immutable and are utilized
 * within various 3D model representations, including but not limited to cubes and other modular structures.
 *
 * Implementing classes or types define specific concrete forms of an `Element`,
 * encapsulating more specialized attributes or behaviors.
 *
 * Examples of `Element` implementations include `Cube`, where the geometric data
 * and specific rendering attributes are extended further.
 *
 * @see Cube
 * @see Model
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Element : Lighting {

    /**
     * The origin point of the 3D element in space.
     *
     * Represents a [Point3d] defining the position of the element in a three-dimensional grid,
     * characterized by its `x`, `y`, and `z` coordinates.
     * This property is typically used to determine or manipulate the spatial location
     * of the corresponding element within its environment.
     */
    @get:JvmName("origin")
    val origin: Point3d

}