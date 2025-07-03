package arc.math

internal data class SimplePoint2d(
    override val x: Double,
    override val y: Double
) : Point2d {

    override fun copy(): Point2d {
        return SimplePoint2d(x, y)
    }

    override fun interpolate(other: Point2d, progress: Float): Point2d {
        require(progress in 0.0..1.0) { "Progress value is not in 0.0..1.0 range!" }

        return withXY(
            (other.x - x) * progress,
            (other.y - y) * progress
        )
    }

    object Factory : Point2d.Factory {
        override fun create(x: Double, y: Double) = SimplePoint2d(x, y)
    }

}