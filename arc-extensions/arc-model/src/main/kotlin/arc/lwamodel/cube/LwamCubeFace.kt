package arc.lwamodel.cube

import arc.Arc
import arc.annotations.TypeFactory
import arc.math.Point2i
import arc.model.cube.CubeFace
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents cube face in LWAM format.
 */
interface LwamCubeFace : CubeFace {

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(uvMin: Point2i, uvMax: Point2i, texture: Int): LwamCubeFace

    }

    companion object {

        /**
         * Create new instance of [LwamCubeFace].
         *
         * @param uvMin Min UV coords in texture atlas.
         * @param uvMax Max UV coords in texture atlas.
         * @param texture ID of texture.
         *
         * @return New instance of [LwamCubeFace].
         */
        @JvmStatic
        fun of(uvMin: Point2i, uvMax: Point2i, texture: Int): LwamCubeFace {
            return Arc.factory<Factory>().create(uvMin, uvMax, texture)
        }

    }

}