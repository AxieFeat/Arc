package arc.model.cube

import arc.Arc
import arc.annotations.ImmutableType
import arc.math.Point2i
import arc.model.Model
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a single face of a cube in a 3D model.
 *
 * @see Cube
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface CubeFace {

    /**
     * Represents the min UV coordinates for a cube face's texture mapping.
     *
     * @see CubeFace
     */
    val uvMin: Point2i

    /**
     * Represents the max UV coordinates for a cube face's texture mapping.
     *
     * @see CubeFace
     */
    val uvMax: Point2i

    /**
     * Rotation of texture.
     */
    val rotation: Float

    /**
     * Is this value set to false - this face will be ignored by [Model.cullFaces].
     */
    @get:JvmName("isCulling")
    val isCullable: Boolean

    @ApiStatus.Internal
    interface Builder : arc.util.pattern.Builder<CubeFace> {

        fun setUvMin(uvMin: Point2i): Builder
        fun setUvMin(u: Int, v: Int): Builder = setUvMin(Point2i.of(u, v))

        fun setUvMax(uvMax: Point2i): Builder
        fun setUvMax(u: Int, v: Int): Builder = setUvMax(Point2i.of(u, v))

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
            return Arc.factory<Builder>()
        }

    }

}