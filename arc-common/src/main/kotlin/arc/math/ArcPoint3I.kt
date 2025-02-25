package arc.math

internal data class ArcPoint3I(
    override var x: Int,
    override var y: Int,
    override var z: Int
) : Point3i {

    object Factory : Point3i.Factory {
        override fun create(x: Int, y: Int, z: Int) = ArcPoint3I(x, y, z)
    }

}