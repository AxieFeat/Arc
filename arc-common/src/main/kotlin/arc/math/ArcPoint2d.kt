package arc.math

import kotlinx.serialization.Serializable

@Serializable
internal data class ArcPoint2d(
    override var x: Double,
    override var y: Double
) : Point2d {

    object Factory : Point2d.Factory {
        override fun create(x: Double, y: Double) = ArcPoint2d(x, y)
    }

}