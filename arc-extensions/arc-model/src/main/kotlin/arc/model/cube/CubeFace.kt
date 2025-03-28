package arc.model.cube

import arc.annotations.ImmutableType
import arc.math.Point2i
import arc.model.texture.ModelTexture

/**
 * Represents a single face of a cube in a 3D model.
 *
 * @see Cube
 * @see Point2i
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface CubeFace {

    /**
     * Represents the UV coordinates for a cube face's texture mapping.
     *
     * @see CubeFace
     */
    @get:JvmName("uv")
    val uv: Point2i

    /**
     * Represents the texture ID associated with a specific cube face.
     *
     * @see ModelTexture
     */
    @get:JvmName("texture")
    val texture: Int

}