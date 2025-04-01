package arc.math

import kotlinx.serialization.Serializable

@Serializable
internal data class ArcPoint2d(
    override var x: Double,
    override var y: Double
) : Point2d {

    override fun copy(): Point2d {
        return ArcPoint2d(x, y)
    }

    override fun interpolate(other: Point2d, progress: Float): Point2d {
        require(progress in 0.0..1.0) { "Progress value is not in 0.0..1.0 range!" }

        this.x += (other.x - x) * progress
        this.y += (other.y - y) * progress

        return this
    }

    object Factory : Point2d.Factory {
        override fun create(x: Double, y: Double) = ArcPoint2d(x, y)
    }

}