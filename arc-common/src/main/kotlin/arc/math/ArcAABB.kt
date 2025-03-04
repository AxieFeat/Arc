package arc.math

// TODO
internal data class ArcAABB(
    override val min: Vec3f,
    override val max: Vec3f
) : AABB {

    override val center: Vec3f
        get() = TODO()

    override fun contains(point: Vec3f): Boolean {
        return false
    }

    override fun intersects(other: AABB): Boolean {
       return false
    }

    object Factory : AABB.Factory {
        override fun create(min: Vec3f, max: Vec3f): AABB {
            return ArcAABB(min, max)
        }

    }

}