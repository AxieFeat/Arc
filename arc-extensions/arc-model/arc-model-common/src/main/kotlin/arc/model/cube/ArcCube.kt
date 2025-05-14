package arc.model.cube

import arc.math.AABB
import arc.math.Vec3f
import arc.model.Face
import arc.util.Color
import java.util.*
import java.util.function.Predicate

internal data class ArcCube(
    override val uuid: UUID = UUID.randomUUID(),
    override var from: Vec3f = Vec3f.of(0f, 0f, 0f),
    override var to: Vec3f = Vec3f.of(0f, 0f, 0f),
    override val pivot: Vec3f = Vec3f.of(0f, 0f, 0f),
    override val rotation: Vec3f = Vec3f.of(0f, 0f, 0f),
    override val lightLevel: Byte = 0,
    override val lightColor: Color = Color.of(),
    override val faces: MutableMap<Face, CubeFace> = mutableMapOf(),
) : Cube {

    // TODO
    override val aabb: AABB = AABB.of(
        from,
        to
    )

    override fun translate(offsetX: Float, offsetY: Float, offsetZ: Float) {
        from = from.add(
            Vec3f.of(offsetX, offsetY, offsetZ)
        )
        to = to.add(
            Vec3f.of(offsetX, offsetY, offsetZ)
        )
    }

    override fun removeFaceIf(filter: Predicate<Map.Entry<Face, CubeFace>>) {
        faces.toMap().forEach {
            if(filter.test(it)) {
                faces.remove(it.key)
            }
        }
    }

    override fun copy(): Cube {
        return ArcCube(
            uuid,
            from.copy(),
            to.copy(),
            pivot.copy(),
            rotation.copy(),
            lightLevel,
            lightColor.copy(),
            faces.toMutableMap()
        )
    }

    class Builder : Cube.Builder {

        private var uuid = UUID.randomUUID()
        private var from = Vec3f.of(0f, 0f, 0f)
        private var to = Vec3f.of(0f, 0f, 0f)
        private var pivot = Vec3f.of(0f, 0f, 0f)
        private var rotation = Vec3f.of(0f, 0f, 0f)
        private var lightLevel: Byte = 0
        private var lightColor: Color = Color.of()
        private val faces = mutableMapOf<Face, CubeFace>()

        override fun setUUID(uuid: UUID): Cube.Builder {
            this.uuid = uuid

            return this
        }

        override fun setFrom(from: Vec3f): Cube.Builder {
            this.from = from
            return this
        }

        override fun setTo(to: Vec3f): Cube.Builder {
            this.to = to
            return this
        }

        override fun setPivot(pivot: Vec3f): Cube.Builder {
            this.pivot = pivot
            return this
        }

        override fun setRotation(rotation: Vec3f): Cube.Builder {
            this.rotation = rotation
            return this
        }

        override fun setLightLevel(lightLevel: Byte): Cube.Builder {
            this.lightLevel = lightLevel
            return this
        }

        override fun setLightColor(lightColor: Color): Cube.Builder {
            this.lightColor = lightColor
            return this
        }

        override fun addFace(face: Face, cubeFace: CubeFace): Cube.Builder {
            faces[face] = cubeFace
            return this
        }

        override fun build(): Cube {
            return ArcCube(uuid, from, to, pivot, rotation, lightLevel, lightColor, faces)
        }

    }


}