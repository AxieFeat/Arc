package arc.lwamodel.cube

import arc.math.Point3d
import arc.model.Face
import arc.model.cube.CubeFace
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
internal data class ArcLWAModelCube(
    @Contextual
    override val uuid: UUID = UUID.randomUUID(),
    override val from: Point3d = Point3d.ZERO,
    override val to: Point3d = Point3d.ZERO,
    override val faces: Map<Face, CubeFace> = mapOf(),
    override val lightLevel: Byte = 0,
    override val lightColor: Int = 0
) : LWAModelCube {

    override val origin: Point3d =
        Point3d.of(
            (from.x + to.x) * 0.5f,
            (from.y + to.y) * 0.5f,
            (from.z + to.z) * 0.5f
        )

    object Factory : LWAModelCube.Factory {
        override fun create(
            uuid: UUID,
            from: Point3d,
            to: Point3d,
            faces: Map<Face, CubeFace>,
            lightLevel: Byte,
            lightColor: Int
        ): LWAModelCube {
            return ArcLWAModelCube(uuid, from, to, faces, lightLevel, lightColor)
        }

    }

}