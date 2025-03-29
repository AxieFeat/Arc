package arc.math

import kotlinx.serialization.Serializable

@Serializable
internal data class ArcPoint3d(
    override var x: Double,
    override var y: Double,
    override var z: Double
) : Point3d {

    object Factory : Point3d.Factory {
        override fun create(x: Double, y: Double, z: Double) = ArcPoint3d(x, y, z)
    }

}