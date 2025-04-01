package arc.math

import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
internal data class ArcPoint2i(
    override var x: Int,
    override var y: Int
) : Point2i {

    override fun copy(): Point2i {
        return ArcPoint2i(x, y)
    }

    override fun interpolate(other: Point2i, progress: Float): Point2i {
        require(progress in 0.0..1.0) { "Progress value is not in 0.0..1.0 range!" }

        this.x += ((other.x - x) * progress).roundToInt()
        this.y += ((other.y - y) * progress).roundToInt()

        return this
    }

    object Factory : Point2i.Factory {
        override fun create(x: Int, y: Int) = ArcPoint2i(x, y)
    }

}