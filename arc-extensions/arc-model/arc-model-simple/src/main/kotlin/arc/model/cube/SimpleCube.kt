package arc.model.cube

import arc.math.AABB
import arc.model.Face
import arc.util.Color
import org.joml.Vector3f
import java.util.*
import java.util.function.Predicate

internal data class SimpleCube(
    override val uuid: UUID = UUID.randomUUID(),
    override var from: Vector3f = Vector3f(),
    override var to: Vector3f = Vector3f(),
    override val pivot: Vector3f = Vector3f(),
    override val rotation: Vector3f = Vector3f(),
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
        from.add(
            Vector3f(offsetX, offsetY, offsetZ)
        )
        to.add(
            Vector3f(offsetX, offsetY, offsetZ)
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
        return SimpleCube(
            uuid,
            Vector3f(from),
            Vector3f(to),
            Vector3f(pivot),
                Vector3f(rotation),
            lightLevel,
            lightColor.copy(),
            faces.toMutableMap()
        )
    }

    class Builder : Cube.Builder {

        private var uuid = UUID.randomUUID()
        private var from = Vector3f()
        private var to = Vector3f()
        private var pivot = Vector3f()
        private var rotation = Vector3f()
        private var lightLevel: Byte = 0
        private var lightColor: Color = Color.of()
        private val faces = mutableMapOf<Face, CubeFace>()

        override fun setUUID(uuid: UUID): Cube.Builder {
            this.uuid = uuid

            return this
        }

        override fun setFrom(from: Vector3f): Cube.Builder {
            this.from = from
            return this
        }

        override fun setTo(to: Vector3f): Cube.Builder {
            this.to = to
            return this
        }

        override fun setPivot(pivot: Vector3f): Cube.Builder {
            this.pivot = pivot
            return this
        }

        override fun setRotation(rotation: Vector3f): Cube.Builder {
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