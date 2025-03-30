package arc.model.cube

import arc.annotations.ImmutableType
import arc.model.Element
import arc.math.Point3d
import arc.model.Face

/**
 * Represents a cube element in a 3D model.
 *
 * @see Element
 * @see CubeFace
 * @see Face
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Cube : Element {

    /**
     * Defines the starting point of the cube within a 3D space.
     */
    @get:JvmName("from")
    val from: Point3d

    /**
     * Represents the endpoint of the cube within a 3D space.
     */
    @get:JvmName("to")
    val to: Point3d

    /**
     * All faces of this cube, that will be rendered.
     */
    @get:JvmName("faces")
    val faces: Map<Face, CubeFace>

}