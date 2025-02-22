package arc.light

import arc.annotations.MutableType
import arc.math.Point3d

/**
 * Represents a mutable light source in a 3D scene. The light source is characterized
 * by its spatial position, intensity (power), and color.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface LightSource : Lighting {

    /**
     * Represents the spatial position of the light source in a 3D space.
     *
     * This property allows getting or setting the position of the light source using
     * a [Point3d] instance, which includes x, y, and z coordinates.
     *
     * Modifying this property affects the position of the light in the 3D scene.
     */
    @get:JvmName("lightPosition")
    var lightPosition: Point3d

}