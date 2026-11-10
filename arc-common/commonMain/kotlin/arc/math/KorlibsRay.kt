package arc.math

import korlibs.math.geom.Vector3F

internal data class KorlibsRay(
    override val origin: Vector3F,
    override val direction: Vector3F
) : Ray {

    override fun getEndPoint(distance: Float): Vector3F {
        return Vector3F(origin.x + direction.x * distance, origin.y + direction.y * distance, origin.z + direction.z * distance)
    }

    override fun copy(): Ray = KorlibsRay(origin, direction)

    object Factory : Ray.Factory {

        override fun create(origin: Vector3F, direction: Vector3F): Ray {
            return KorlibsRay(origin, direction)
        }
    }
}
