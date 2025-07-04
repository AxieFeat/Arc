package arc.math

import org.joml.Vector3f

internal data class JomlAABB(
    override val min: Vector3f,
    override val max: Vector3f,
    override val center: Vector3f
) : AABB {

    override fun contains(point: Vector3f): Boolean {
        return point.x in min.x..max.x &&
                point.y in min.y..max.y &&
                point.z in min.z..max.z
    }

    override fun intersects(other: AABB): Boolean {
        return min.x <= other.max.x && max.x >= other.min.x &&
                min.y <= other.max.y && max.y >= other.min.y &&
                min.z <= other.max.z && max.z >= other.min.z
    }

    object Factory : AABB.Factory {
        override fun create(min: Vector3f, max: Vector3f, center: Vector3f): AABB {
            return JomlAABB(min, max, center)
        }
    }
}