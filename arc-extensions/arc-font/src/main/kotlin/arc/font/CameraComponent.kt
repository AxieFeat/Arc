package arc.font

import arc.graphics.Camera
import net.kyori.adventure.text.Component
import org.joml.Matrix4f

/**
 * This class represents 3d component, that handles events by camera.
 *
 * You can use any transformation via matrix, because this class use ray from camera.
 */
data class CameraComponent(
    override val component: Component,
    override val matrix: Matrix4f = Matrix4f(),
    override val size: Int = 12
) : WrappedComponent {

    override val width: Int = 0
    override val height: Int = 0

    /**
     * Update component with camera for handling events.
     *
     * @param camera Camera for update component.
     */
    fun update(camera: Camera) {
        // TODO
    }

}