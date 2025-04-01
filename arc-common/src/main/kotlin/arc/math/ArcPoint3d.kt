package arc.math

import kotlinx.serialization.Serializable

@Serializable
internal data class ArcPoint3d(
    override var x: Double,
    override var y: Double,
    override var z: Double
) : Point3d {

    override fun copy(): Point3d {
        return ArcPoint3d(x, y, z)
    }

    override fun interpolate(other: Point3d, progress: Float): Point3d {
        require(progress in 0.0..1.0) { "Progress value is not in 0.0..1.0 range!" }

        this.x += (other.x - x) * progress
        this.y += (other.y - y) * progress
        this.z += (other.z - z) * progress

        return this
    }

    object Factory : Point3d.Factory {
        override fun create(x: Double, y: Double, z: Double) = ArcPoint3d(x, y, z)
    }

}