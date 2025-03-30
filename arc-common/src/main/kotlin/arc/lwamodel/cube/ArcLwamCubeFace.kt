package arc.lwamodel.cube

import arc.math.Point2i
import kotlinx.serialization.Serializable

@Serializable
internal data class ArcLwamCubeFace(
    override val uvMin: Point2i,
    override val uvMax: Point2i,
    override val texture: Int = 0
) : LwamCubeFace {

    object Factory : LwamCubeFace.Factory {
        override fun create(uvMin: Point2i, uvMax: Point2i, texture: Int): LwamCubeFace {
            return ArcLwamCubeFace(uvMin, uvMax, texture)
        }
    }

}