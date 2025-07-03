package arc.model.cube

import arc.math.Point2i

internal data class SimpleCubeFace(
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

        override fun setUvMin(uvMin: Point2i): CubeFace.Builder {
            this.uvMin = uvMin

            return this
        }

        override fun setUvMax(uvMax: Point2i): CubeFace.Builder {
            this.uvMax = uvMax

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
            return SimpleCubeFace(uvMin, uvMax, rotation, isCullable)
        }

    }

}