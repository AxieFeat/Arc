package arc.math

import kotlin.math.roundToInt

internal data class SimplePoint3i(
    override val x: Int,
    override val y: Int,
    override val z: Int
) : Point3i {

    override fun copy(): Point3i {
        return SimplePoint3i(x, y, z)
    }

    override fun interpolate(other: Point3i, progress: Float): Point3i {
        require(progress in 0.0..1.0) { "Progress value is not in 0.0..1.0 range!" }

        return withXYZ(
            ((other.x - x) * progress).roundToInt(),
            ((other.y - y) * progress).roundToInt(),
            ((other.z - z) * progress).roundToInt()
        )
    }

    object Factory : Point3i.Factory {
        override fun create(x: Int, y: Int, z: Int) = SimplePoint3i(x, y, z)
    }

}