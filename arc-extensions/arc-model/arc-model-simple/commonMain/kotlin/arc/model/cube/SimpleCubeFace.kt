package arc.model.cube

import korlibs.math.geom.Vector2I

internal data class SimpleCubeFace(
    override val uvMin: Vector2I = Vector2I(0, 0),
    override val uvMax: Vector2I = Vector2I(0, 0),
    override val rotation: Float = 0f,
    override val isCullable: Boolean = true
) : CubeFace {

    class Builder : CubeFace.Builder {

        private var uvMin = Vector2I(0, 0)
        private var uvMax = Vector2I(0, 0)
        private var rotation = 0f
        private var isCullable = true

        override fun setUvMin(uvMin: Vector2I): CubeFace.Builder {
            this.uvMin = uvMin

            return this
        }

        override fun setUvMax(uvMax: Vector2I): CubeFace.Builder {
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
