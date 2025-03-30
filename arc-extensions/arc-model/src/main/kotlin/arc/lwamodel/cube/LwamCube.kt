package arc.lwamodel.cube

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.lwamodel.LwamElement
import arc.math.Point3d
import arc.model.Face
import arc.model.cube.Cube
import arc.model.cube.CubeFace
import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * This interface represents cube in LWAM format.
 */
@ImmutableType
interface LwamCube : Cube, LwamElement {

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(
            uuid: UUID,
            from: Point3d,
            to: Point3d,
            faces: Map<Face, CubeFace>,
            lightLevel: Byte,
            lightColor: Int
        ) : LwamCube

    }


    companion object {

        /**
         * Create new instance of [LwamCube].
         *
         * @param uuid UUID of cube.
         * @param from Start position of point.
         * @param to End position of point.
         * @param faces Faces of cube.
         * @param origin Center point of cube.
         * @param lightLevel Light level of cube.
         * @param lightColor Light color of cube.
         *
         * @return New instance of [LwamCube].
         */
        @JvmStatic
        fun of(
            uuid: UUID = UUID.randomUUID(),
            from: Point3d = Point3d.ZERO,
            to: Point3d = Point3d.ZERO,
            faces: Map<Face, CubeFace> = mapOf(),
            lightLevel: Byte = 0,
            lightColor: Int = 0
        ): LwamCube {
            return Arc.factory<Factory>().create(uuid, from, to, faces, lightLevel, lightColor)
        }

    }

}