package arc.model.cube

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.model.Element
import arc.math.Point3d
import arc.model.Face
import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * Represents a cube element in a 3D model.
 *
 * @see Element
 * @see CubeFace
 * @see Face
 */
@ImmutableType
interface Cube : Element {

    /**
     * Defines the starting point of the cube within a 3D space.
     */
    val from: Point3d

    /**
     * Represents the endpoint of the cube within a 3D space.
     */
    val to: Point3d

    /**
     * All faces of this cube, that will be rendered.
     */
    val faces: Map<Face, arc.model.cube.CubeFace>

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(
            uuid: UUID,
            from: Point3d,
            to: Point3d,
            faces: Map<Face, arc.model.cube.CubeFace>,
            lightLevel: Byte,
            lightColor: Int
        ) : arc.model.cube.Cube

    }


    companion object {

        /**
         * Create new instance of [Cube].
         *
         * @param uuid UUID of cube.
         * @param from Start position of point.
         * @param to End position of point.
         * @param faces Faces of cube.
         * @param lightLevel Light level of cube.
         * @param lightColor Light color of cube.
         *
         * @return New instance of [Cube].
         */
        @JvmStatic
        fun of(
            uuid: UUID = UUID.randomUUID(),
            from: Point3d = Point3d.ZERO,
            to: Point3d = Point3d.ZERO,
            faces: Map<Face, arc.model.cube.CubeFace> = mapOf(),
            lightLevel: Byte = 0,
            lightColor: Int = 0
        ): arc.model.cube.Cube {
            return Arc.factory<arc.model.cube.Cube.Factory>().create(uuid, from, to, faces, lightLevel, lightColor)
        }

    }

}