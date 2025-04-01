package arc.math

import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
internal data class ArcPoint3i(
    override var x: Int,
    override var y: Int,
    override var z: Int
) : Point3i {

    override fun copy(): Point3i {
        return ArcPoint3i(x, y, z)
    }

    override fun interpolate(other: Point3i, progress: Float): Point3i {
        require(progress in 0.0..1.0) { "Progress value is not in 0.0..1.0 range!" }

        this.x += ((other.x - x) * progress).roundToInt()
        this.y += ((other.y - y) * progress).roundToInt()
        this.z += ((other.z - z) * progress).roundToInt()

        return this
    }

    object Factory : Point3i.Factory {
        override fun create(x: Int, y: Int, z: Int) = ArcPoint3i(x, y, z)
    }

}