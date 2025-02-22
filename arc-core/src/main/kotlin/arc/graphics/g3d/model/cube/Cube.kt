package arc.graphics.g3d.model.cube

import arc.annotations.ImmutableType
import arc.graphics.g3d.Face
import arc.graphics.g3d.model.Element
import arc.graphics.g3d.model.Model
import arc.math.Point3d

/**
 * Represents a cube element within a 3D model structure.
 *
 * A `Cube` is defined by two points in a 3D space, representing diagonal corners
 * of the cube's bounding box, and encompasses six distinct faces. Each face
 * is associated with a specific texture and UV mapping, providing a visual
 * representation of the cube.
 *
 * The cube is immutable and extends the `Element` interface, inheriting
 * essential properties such as lighting and origin attributes. It serves as
 * a fundamental building block in constructing and rendering 3D models.
 *
 * @see Element
 * @see CubeFace
 * @see Point3d
 * @see Face
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Cube : Element {

    /**
     * Defines the starting point of the cube within a 3D space.
     *
     * This property represents one corner of the cube's bounding box, specified as a [Point3d].
     * The `from` coordinate, along with the `to` property, helps in determining the spatial
     * representation, dimensions, and orientation of the cube. It serves as one of the
     * two diagonal points necessary for defining a cube's geometry.
     *
     * The `from` point is immutable, ensuring consistent definition and rendering
     * throughout the cube's lifecycle.
     *
     * @see Point3d
     */
    @get:JvmName("from")
    val from: Point3d

    /**
     * Represents the endpoint of the cube within a 3D space.
     *
     * This property defines the second corner of the cube's bounding box as a [Point3d],
     * complementing the `from` property to establish the cube's spatial boundaries.
     * The combination of `from` and `to` determines the dimensions, orientation, and
     * overall geometry of the cube.
     *
     * The `to` point is immutable, ensuring consistent spatial definition and rendering
     * of the cube throughout its lifecycle.
     *
     * @see Point3d
     */
    @get:JvmName("to")
    val to: Point3d

    /**
     * A mapping of directional faces to their corresponding representations on a cube.
     *
     * This property defines the association between the six directional faces (`Face`)
     * in a 3D coordinate system and their respective `CubeFace` implementations. Each
     * entry in the map represents a specific face of the cube and its related
     * rendering or texture data.
     *
     * The keys of the map are `Face` values (e.g., `NORTH`, `EAST`), which represent
     * the directional orientation of a cube's face. The values are `CubeFace` objects
     * that define the attributes of each cube face, such as texture ID and UV mapping.
     */
    @get:JvmName("faces")
    val faces: Map<Face, CubeFace>

}