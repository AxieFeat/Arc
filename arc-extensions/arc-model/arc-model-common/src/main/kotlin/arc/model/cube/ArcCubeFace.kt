package arc.model.cube

import arc.math.Point2i

internal data class ArcCubeFace(
    override val uvMin: Point2i,
    override val uvMax: Point2i,
    override val texture: Int = 0
) : CubeFace {

    object Factory : CubeFace.Factory {
        override fun create(uvMin: Point2i, uvMax: Point2i, texture: Int): CubeFace {
            return ArcCubeFace(uvMin, uvMax, texture)
        }
    }

}