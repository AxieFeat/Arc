package arc.graphics.g3d.model

import arc.annotations.ImmutableType
import arc.math.Point3d
import arc.graphics.g3d.model.cube.Cube

/**
 * Represents a structural or functional component of a 3D model.
 *
 * An `Element` provides essential characteristics such as its lighting properties
 * and its origin position in a 3D space. These components are immutable and are utilized
 * within various 3D model representations, including but not limited to cubes and other modular structures.
 *
 * Implementing classes or types define specific concrete forms of an `Element`,
 * encapsulating more specialized attributes or behaviors.
 *
 * Examples of `Element` implementations include `Cube`, where the geometric data
 * and specific rendering attributes are extended further.
 *
 * @see Cube
 * @see Model
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Element {

    /**
     * Represents the light emission level of an element.
     *
     * The light level is a value ranging from 0 to a maximum of 15, where 0 indicates no light emission
     * and 15 represents the highest level of light emission. This property is immutable and defines
     * the intensity of light that an element emits within a 3D model.
     *
     * Light levels are typically used in rendering processes to simulate realistic illumination
     * and shading effects within 3D environments.
     */
    @get:JvmName("lightLevel")
    val lightLevel: Byte

    /**
     * Represents the light color associated with an element in a 3D model.
     *
     * This value determines the RGB color of the light emitted by the element. It is encoded as an
     * integer where the RGB components are packed into a 32-bit value, following the ARGB format (Alpha-Red-Green-Blue)
     * with the alpha channel typically set to full opacity. The specific color determines the visual hue
     * and tone of the light in rendering processes.
     *
     * This property is immutable, ensuring that the light color remains constant for the lifetime
     * of the element.
     */
    @get:JvmName("lightColor")
    val lightColor: Int

    /**
     * The origin point of the 3D element in space.
     *
     * Represents a [Point3d] defining the position of the element in a three-dimensional grid,
     * characterized by its `x`, `y`, and `z` coordinates.
     * This property is typically used to determine or manipulate the spatial location
     * of the corresponding element within its environment.
     */
    @get:JvmName("origin")
    val origin: Point3d

}