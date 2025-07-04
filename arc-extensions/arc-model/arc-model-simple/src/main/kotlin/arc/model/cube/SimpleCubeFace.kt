package arc.model.cube

import org.joml.Vector2i

internal data class SimpleCubeFace(
    override val uvMin: Vector2i = Vector2i(),
    override val uvMax: Vector2i = Vector2i(),
    override val rotation: Float = 0f,
    override val isCullable: Boolean = true
) : CubeFace {

    class Builder : CubeFace.Builder {

        private var uvMin = Vector2i()
        private var uvMax = Vector2i()
        private var rotation = 0f
        private var isCullable = true

        override fun setUvMin(uvMin: Vector2i): CubeFace.Builder {
            this.uvMin = uvMin

            return this
        }

        override fun setUvMax(uvMax: Vector2i): CubeFace.Builder {
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