package arc.model.cube

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.math.Point2i
import arc.model.texture.ModelTexture
import org.jetbrains.annotations.ApiStatus

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
    val uvMin: Point2i

    /**
     * Represents the max UV coordinates for a cube face's texture mapping.
     *
     * @see CubeFace
     */
    val uvMax: Point2i

    /**
     * Represents the texture ID associated with a specific cube face.
     *
     * @see ModelTexture
     */
    val texture: Int

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(uvMin: Point2i, uvMax: Point2i, texture: Int): arc.model.cube.CubeFace

    }

    companion object {

        /**
         * Create new instance of [CubeFace].
         *
         * @param uvMin Min UV coords in texture atlas.
         * @param uvMax Max UV coords in texture atlas.
         * @param texture ID of texture.
         *
         * @return New instance of [CubeFace].
         */
        @JvmStatic
        fun of(uvMin: Point2i, uvMax: Point2i, texture: Int): arc.model.cube.CubeFace {
            return Arc.factory<arc.model.cube.CubeFace.Factory>().create(uvMin, uvMax, texture)
        }

    }

}