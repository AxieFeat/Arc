package arc.model.cube

import arc.annotations.ImmutableType
import arc.math.Point2i
import arc.model.texture.ModelTexture

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

}