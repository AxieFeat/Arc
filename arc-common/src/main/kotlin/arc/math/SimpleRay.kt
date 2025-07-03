package arc.math

internal data class SimpleRay(
    override var origin: Vec3f,
    override var direction: Vec3f
) : Ray {

    override fun getEndPoint(distance: Float): Vec3f {
        return direction.scl(distance).add(origin)
    }

    override fun copy(): Ray = SimpleRay(origin, direction)

    object Factory : Ray.Factory {
        override fun create(origin: Vec3f, direction: Vec3f): Ray {
            return SimpleRay(origin, direction)
        }

    }

}