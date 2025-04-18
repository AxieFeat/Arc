package arc.model.cube

import arc.math.Point2i

internal data class ArcCubeFace(
    override val uvMin: Point2i = Point2i.of(0, 0),
    override val uvMax: Point2i = Point2i.of(0, 0),
    override val rotation: Float = 0f,
    override val isCullable: Boolean = true
) : CubeFace {

    class Builder : CubeFace.Builder {

        private var uvMin = Point2i.of(0, 0)
        private var uvMax = Point2i.of(0, 0)
        private var rotation = 0f
        private var isCullable = true

        override fun setUvMin(u: Int, v: Int): CubeFace.Builder {
            uvMin.apply {
                this.x = u
                this.y = v
            }

            return this
        }

        override fun setUvMax(u: Int, v: Int): CubeFace.Builder {
            uvMax.apply {
                this.x = u
                this.y = v
            }

            return this
        }

        override fun rotation(rotation: Float): CubeFace.Builder {
            this.rotation = rotation

            return this
        }

        override fun setCull(cullable: Boolean): CubeFace.Builder {
            isCullable = true

            return this
        }

        override fun build(): CubeFace {
            return ArcCubeFace(uvMin, uvMax, rotation, isCullable)
        }
    }

}