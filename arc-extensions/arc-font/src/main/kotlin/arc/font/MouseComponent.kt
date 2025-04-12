package arc.font

import arc.input.mouse.MouseInput
import net.kyori.adventure.text.Component
import org.joml.Matrix4f

/**
 * This class represents 2d component, that handles events by mouse.
 *
 * Not use powerful transformation via matrix, because it can break calling of events (Like hovering or click).
 */
data class MouseComponent(
    override val component: Component,
    override val matrix: Matrix4f = Matrix4f(),
    override val size: Int = 12
) : WrappedComponent {

    override val width: Int = 0
    override val height: Int = 0

    /**
     * Update component with mouse for handling events.
     *
     * @param mouse Mouse for update component.
     */
    fun update(mouse: MouseInput) {
        // TODO
    }

}