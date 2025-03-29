package arc.lwamodel.cube

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.lwamodel.LWAModelElement
import arc.math.Point3d
import arc.model.Face
import arc.model.cube.Cube
import arc.model.cube.CubeFace
import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * This interface represents cube in LWA model format.
 */
@ImmutableType
interface LWAModelCube : Cube, LWAModelElement {

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(
            uuid: UUID = UUID.randomUUID(),
            from: Point3d = Point3d.ZERO,
            to: Point3d = Point3d.ZERO,
            faces: Map<Face, CubeFace> = mapOf(),
            lightLevel: Byte = 0,
            lightColor: Int = 0
        ) : LWAModelCube

    }


    companion object {

        /**
         * Create new instance of [LWAModelCube].
         *
         * @param uuid UUID of cube.
         * @param from Start position of point.
         * @param to End position of point.
         * @param faces Faces of cube.
         * @param origin Center point of cube.
         * @param lightLevel Light level of cube.
         * @param lightColor Light color of cube.
         *
         * @return New instance of [LWAModelCube].
         */
        @JvmStatic
        fun of(
            uuid: UUID = UUID.randomUUID(),
            from: Point3d = Point3d.ZERO,
            to: Point3d = Point3d.ZERO,
            faces: Map<Face, CubeFace> = mapOf(),
            lightLevel: Byte = 0,
            lightColor: Int = 0
        ): LWAModelCube {
            return Arc.factory<Factory>().create(uuid, from, to, faces, lightLevel, lightColor)
        }

    }

}