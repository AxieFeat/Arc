package arc.math

import kotlin.math.roundToInt

internal data class ArcPoint2i(
    override val x: Int,
    override val y: Int
) : Point2i {

    override fun copy(): Point2i {
        return ArcPoint2i(x, y)
    }

    override fun interpolate(other: Point2i, progress: Float): Point2i {
        require(progress in 0.0..1.0) { "Progress value is not in 0.0..1.0 range!" }

        return withXY(
            ((other.x - x) * progress).roundToInt(),
            ((other.y - y) * progress).roundToInt()
        )
    }

    object Factory : Point2i.Factory {
        override fun create(x: Int, y: Int) = ArcPoint2i(x, y)
    }

}