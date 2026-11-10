package arc.model.cube

import arc.Arc.factory
import arc.annotations.ImmutableType
import arc.annotations.InternalApi
import korlibs.math.geom.Vector2I
import kotlin.jvm.JvmStatic

/**
 * Represents a single face of a cube in a 3D model.
 *
 * @see Cube
 */
@ImmutableType
interface CubeFace {

    /**
     * Represents the min UV coordinates for a cube face's texture mapping.
     *
     * @see CubeFace
     */
    val uvMin: Vector2I

    /**
     * Represents the max UV coordinates for a cube face's texture mapping.
     *
     * @see CubeFace
     */
    val uvMax: Vector2I

    /**
     * Rotation of texture.
     */
    val rotation: Float

    /**
     * Is this value set to false - this face will be ignored by [Model.cullFaces].
     */
    val isCullable: Boolean

    @InternalApi
    interface Builder : arc.util.pattern.Builder<CubeFace> {

        fun setUvMin(uvMin: Vector2I): Builder
        fun setUvMin(u: Int, v: Int): Builder = setUvMin(Vector2I(u, v))

        fun setUvMax(uvMax: Vector2I): Builder
        fun setUvMax(u: Int, v: Int): Builder = setUvMax(Vector2I(u, v))

        fun rotation(rotation: Float): Builder

        fun setCull(cullable: Boolean): Builder
    }

    companion object {

        /**
         * Create new instance of [CubeFace] via builder.
         *
         * @return New instance of [Builder].
         */
        @JvmStatic
        fun builder(): Builder {
            return factory<Builder>()
        }
    }
}
