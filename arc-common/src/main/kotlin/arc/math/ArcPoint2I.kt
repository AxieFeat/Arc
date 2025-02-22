package arc.math

data class ArcPoint2I(
    override var x: Int,
    override var y: Int
) : Point2i {

    object Factory : Point2i.Factory {
        override fun create(x: Int, y: Int) = ArcPoint2I(x, y)
    }

}