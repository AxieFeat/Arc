package arc.lwamodel.cube

import arc.math.Point2i
import kotlinx.serialization.Serializable

@Serializable
internal data class ArcLWAModelCubeFace(
    override val uvMin: Point2i,
    override val uvMax: Point2i,
    override val texture: Int = 0
) : LWAModelCubeFace {

    object Factory : LWAModelCubeFace.Factory {
        override fun create(uvMin: Point2i, uvMax: Point2i, texture: Int): LWAModelCubeFace {
            return ArcLWAModelCubeFace(uvMin, uvMax, texture)
        }
    }

}