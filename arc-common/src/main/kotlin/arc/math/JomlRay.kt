package arc.math

import org.joml.Vector3f

internal data class JomlRay(
    override var origin: Vector3f,
    override var direction: Vector3f
) : Ray {

    override fun getEndPoint(distance: Float): Vector3f {
        return Vector3f(direction).mul(distance).add(origin)
    }

    override fun copy(): Ray = JomlRay(origin, direction)

    object Factory : Ray.Factory {
        override fun create(origin: Vector3f, direction: Vector3f): Ray {
            return JomlRay(origin, direction)
        }
    }

}