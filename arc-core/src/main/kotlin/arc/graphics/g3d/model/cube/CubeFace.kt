package arc.graphics.g3d.model.cube

import arc.annotations.ImmutableType
import arc.math.Point2i
import arc.graphics.g3d.Face

/**
 * Represents a single face of a cube in a 3D model.
 *
 * A `CubeFace` details the texture information associated with one of the six faces
 * of a cube. This interface defines the specific UV coordinates within a texture and
 * the texture ID to be applied to the face. The immutability ensures that the face's
 * attributes cannot be modified after creation, providing consistency in rendering.
 *
 * The cube face serves as a building block within the `Cube` structure, associating
 * directional faces with specific texture mappings.
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
     * The `uv` property defines a [Point2i] that represents the two-dimensional coordinates used
     * in texture mapping. These coordinates specify the position of the texture to be applied
     * to the cube face. The `uv` coordinates are immutable, ensuring consistency in texture
     * alignment across the cube's lifecycle.
     *
     * @see Point2i
     * @see CubeFace
     */
    @get:JvmName("uv")
    val uv: Point2i

    /**
     * Represents the texture ID associated with a specific cube face.
     *
     * The `texture` property identifies the unique texture resource to be applied to
     * a cube face. The texture ID is used as a reference to retrieve or manage
     * texture data, ensuring accurate rendering of the corresponding face within a
     * 3D model. This value is immutable to maintain consistency across the cube's
     * lifecycle.
     *
     * @see CubeFace
     * @see Face
     */
    @get:JvmName("texture")
    val texture: Int

}