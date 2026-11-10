package arc.model.cube

import arc.math.AABB
import arc.model.Face
import arc.util.Color
import korlibs.math.geom.Vector3F
import kotlin.uuid.Uuid

internal data class SimpleCube(
    override val uuid: Uuid = Uuid.random(),
    override var from: Vector3F = Vector3F(0f, 0f, 0f),
    override var to: Vector3F = Vector3F(0f, 0f, 0f),
    override val pivot: Vector3F = Vector3F(0f, 0f, 0f),
    override val rotation: Vector3F = Vector3F(0f, 0f, 0f),
    override val lightLevel: Byte = 0,
    override val lightColor: Color = Color.of(),
    override val faces: MutableMap<Face, CubeFace> = mutableMapOf(),
) : Cube {

    // TODO
    override val aabb: AABB = AABB.of(
        Vector3F(from.x, from.y, from.z),
        Vector3F(to.x, to.y, to.z)
    )

    override fun translate(offsetX: Float, offsetY: Float, offsetZ: Float) {
        from = Vector3F(from.x + offsetX, from.y + offsetY, from.z + offsetZ)
        to = Vector3F(to.x + offsetX, to.y + offsetY, to.z + offsetZ)
    }

    override fun removeFaceIf(filter: (Map.Entry<Face, CubeFace>) -> Boolean) {
        faces.toMap().forEach {
            if(filter(it)) {
                faces.remove(it.key)
            }
        }
    }

    override fun copy(): Cube {
        return SimpleCube(
            uuid,
            from,
            to,
            pivot,
                rotation,
            lightLevel,
            lightColor.copy(),
            faces.toMutableMap()
        )
    }

    class Builder : Cube.Builder {

        private var uuid = Uuid.random()
        private var from = Vector3F(0f, 0f, 0f)
        private var to = Vector3F(0f, 0f, 0f)
        private var pivot = Vector3F(0f, 0f, 0f)
        private var rotation = Vector3F(0f, 0f, 0f)
        private var lightLevel: Byte = 0
        private var lightColor: Color = Color.of()
        private val faces = mutableMapOf<Face, CubeFace>()

        override fun setUUID(uuid: Uuid): Cube.Builder {
            this.uuid = uuid

            return this
        }

        override fun setFrom(from: Vector3F): Cube.Builder {
            this.from = from
            return this
        }

        override fun setTo(to: Vector3F): Cube.Builder {
            this.to = to
            return this
        }

        override fun setPivot(pivot: Vector3F): Cube.Builder {
            this.pivot = pivot
            return this
        }

        override fun setRotation(rotation: Vector3F): Cube.Builder {
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
            return SimpleCube(uuid, from, to, pivot, rotation, lightLevel, lightColor, faces)
        }

    }


}
