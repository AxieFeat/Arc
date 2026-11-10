package arc.math

import korlibs.math.geom.Vector3F

internal data class KorlibsAABB(
    override val min: Vector3F,
    override val max: Vector3F,
    override val center: Vector3F
) : AABB {

    override fun contains(point: Vector3F): Boolean {
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

        override fun create(min: Vector3F, max: Vector3F, center: Vector3F): AABB {
            return KorlibsAABB(min, max, center)
        }
    }
}
