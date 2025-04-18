package arc.model.cube

import arc.math.AABB
import arc.math.Vec3f
import arc.model.Face
import arc.util.Color
import java.util.*
import java.util.function.Predicate

internal data class ArcCube(
    override val uuid: UUID = UUID.randomUUID(),
    override val from: Vec3f = Vec3f.of(0f, 0f, 0f),
    override val to: Vec3f = Vec3f.of(0f, 0f, 0f),
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
        from.apply {
            this.x += offsetX
            this.y += offsetY
            this.z += offsetZ
        }
        to.apply {
            this.x += offsetX
            this.y += offsetY
            this.z += offsetZ
        }
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
        private val from = Vec3f.of(0f, 0f, 0f)
        private val to = Vec3f.of(0f, 0f, 0f)
        private val pivot = Vec3f.of(0f, 0f, 0f)
        private val rotation = Vec3f.of(0f, 0f, 0f)
        private var lightLevel: Byte = 0
        private var lightColor: Color = Color.of()
        private val faces = mutableMapOf<Face, CubeFace>()

        override fun setUUID(uuid: UUID): Cube.Builder {
            this.uuid = uuid

            return this
        }

        override fun setFrom(x: Float, y: Float, z: Float): Cube.Builder {
            from.apply {
                this.x = x
                this.y = y
                this.z = z
            }
            return this
        }

        override fun setTo(x: Float, y: Float, z: Float): Cube.Builder {
            to.apply {
                this.x = x
                this.y = y
                this.z = z
            }
            return this
        }

        override fun setPivot(x: Float, y: Float, z: Float): Cube.Builder {
            pivot.apply {
                this.x = x
                this.y = y
                this.z = z
            }
            return this
        }

        override fun setRotation(x: Float, y: Float, z: Float): Cube.Builder {
            rotation.apply {
                this.x = x
                this.y = y
                this.z = z
            }
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