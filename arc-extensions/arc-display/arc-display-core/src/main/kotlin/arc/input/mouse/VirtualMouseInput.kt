package arc.input.mouse

import arc.Arc.single
import arc.annotations.TypeFactory
import arc.display.Display
import arc.graphics.Camera
import arc.input.BindingProcessor
import org.jetbrains.annotations.ApiStatus
import org.joml.Vector2f

/**
 * This interface represents virtual mouse. It means, that X and Y coords of this mouse is not real.
 */
interface VirtualMouseInput : MouseInput {

    override val bindingProcessor: BindingProcessor
        get() = throw UnsupportedOperationException("You can not use binding processor for virtual mouse input.")

    /**
     * Position of cursor in display.
     *
     * It will be (-1, -1), if camera not see to display.
     */
    override var position: Vector2f

    /**
     * Display of this virtual mouse.
     */
    val display: Display

    /**
     * Update X and Y positions in mouse.
     *
     * @param camera Camera for updating mouse.
     */
    fun update(camera: Camera)

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(display: Display): VirtualMouseInput
    }

    companion object {

        @JvmStatic
        fun of(display: Display): VirtualMouseInput {
            return single<Factory>().create(display)
        }
    }
}
