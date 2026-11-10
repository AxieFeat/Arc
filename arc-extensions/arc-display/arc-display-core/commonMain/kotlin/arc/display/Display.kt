package arc.display

import arc.Arc.single
import arc.annotations.TypeFactory
import arc.graphics.Camera
import arc.graphics.EmptyShaderInstance
import arc.input.mouse.VirtualMouseInput
import arc.shader.ShaderInstance
import arc.util.pattern.Bindable
import arc.util.pattern.Cleanable
import arc.annotations.InternalApi
import korlibs.math.geom.Matrix4
import kotlin.jvm.JvmStatic

/**
 * This interface represents Virtual display in 3D space.
 */
interface Display : Cleanable, Bindable {

    /**
     * Matrix for transformation display.
     */
    var matrix: Matrix4

    /**
     * Width of display in pixels.
     */
    val width: Int

    /**
     * Height of display in pixels.
     */
    val height: Int

    /**
     * Virtual mouse on this display.
     */
    val mouse: VirtualMouseInput

    /**
     * Update this display via real camera. With values from camera will be calculated position of [mouse].
     *
     * @param camera Camera of some scene.
     */
    fun update(camera: Camera) = mouse.update(camera)

    /**
     * Render this display without any shader (You need it bind your own).
     */
    fun render() = render(EmptyShaderInstance)

    /**
     * Render this display with some shader instance.
     *
     * @param shader Shader for rendering.
     */
    fun render(shader: ShaderInstance)

    /**
     * Clear the display.
     */
    fun clear()

    @TypeFactory
    @InternalApi
    interface Factory {

        fun create(width: Int, height: Int): Display
    }

    companion object {

        /**
         * Creates new [Display].
         *
         * @param width Width of display.
         * @param height Height of display.
         *
         * @return New instance of [Display].
         */
        @JvmStatic
        fun of(width: Int, height: Int): Display {
            return single<Factory>().create(width, height)
        }
    }
}
