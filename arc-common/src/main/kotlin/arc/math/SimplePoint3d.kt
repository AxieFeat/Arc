package arc.math

internal data class SimplePoint3d(
    override val x: Double,
    override val y: Double,
    override val z: Double
) : Point3d {

    override fun copy(): Point3d {
        return SimplePoint3d(x, y, z)
    }

    override fun interpolate(other: Point3d, progress: Float): Point3d {
        require(progress in 0.0..1.0) { "Progress value is not in 0.0..1.0 range!" }

        return withXYZ(
            (other.x - x) * progress,
            (other.y - y) * progress,
            (other.z - z) * progress
        )
    }

    object Factory : Point3d.Factory {
        override fun create(x: Double, y: Double, z: Double) = SimplePoint3d(x, y, z)
    }

}