package arc.math

internal data class ArcAABB(
    override val min: Vec3f,
    override val max: Vec3f,
    override val center: Vec3f
) : AABB {

    override fun contains(point: Vec3f): Boolean {
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
        override fun create(min: Vec3f, max: Vec3f, center: Vec3f): AABB {
            return ArcAABB(min, max, center)
        }
    }
}