package arc.display

import arc.Arc
import arc.annotations.TypeFactory
import arc.graphics.Camera
import arc.graphics.EmptyShaderInstance
import arc.input.mouse.VirtualMouseInput
import arc.math.AABB
import arc.shader.ShaderInstance
import arc.util.pattern.Bindable
import arc.util.pattern.Cleanable
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents Virtual display in 3D space.
 */
interface Display : Cleanable, Bindable {

    /**
     * Bounding box of this display.
     */
    val aabb: AABB

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
     * Render this display without any shader (You need it bind your own)
     */
    fun render() = render(EmptyShaderInstance)

    /**
     * Render this display with some shader instance.
     *
     * @param shader Shader for rendering.
     */
    fun render(shader: ShaderInstance)

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(aabb: AABB): Display

    }

    companion object {

        /**
         * Creates new [Display].
         *
         * @param aabb Bounding box of display. Via min and max point will be created display.
         *
         * @return New instance of [Display].
         */
        @JvmStatic
        fun from(aabb: AABB): Display {
            return Arc.factory<Factory>().create(aabb)
        }

    }


}