package arc.math

internal data class ArcPoint2i(
    override var x: Int,
    override var y: Int
) : Point2i {

    object Factory : Point2i.Factory {
        override fun create(x: Int, y: Int) = ArcPoint2i(x, y)
    }

}