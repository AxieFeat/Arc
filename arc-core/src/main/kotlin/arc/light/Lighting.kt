package arc.light

import arc.annotations.MutableType
import arc.util.Color

/**
 * Represents a lighting configuration that controls the properties of a light source in a 3D scene.
 *
 * This interface provides methods to adjust the intensity and color of the lighting.
 * Implementations of this interface are expected to allow for dynamic real-time updates
 * to the light attributes, enabling flexible visual effects in 3D environments.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Lighting {

    /**
     * Represents the intensity or brightness of the light source.
     *
     * The `lightLevel` property defines the output level of the light and is used to control
     * the brightness of the light in the 3D scene. Its value has range between 0 and 15.
     *
     * Adjusting this value changes the light intensity, directly affecting the appearance
     * of the illuminated objects within the scene.
     */
    @get:JvmName("lightLevel")
    var lightLevel: Byte

    /**
     * Represents the color of the light source.
     *
     * The `lightColor` property defines the visible color emitted by the light source and can
     * be set or retrieved as a [Color] instance. Changing this property affects how the
     * light illuminates objects within the 3D scene.
     */
    @get:JvmName("lightColor")
    var lightColor: Color

}