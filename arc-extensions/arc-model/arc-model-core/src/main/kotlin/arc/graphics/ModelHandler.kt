package arc.graphics

import arc.Application
import arc.Arc.single
import arc.annotations.TypeFactory
import arc.math.AABB
import arc.model.Model
import arc.shader.ShaderInstance
import arc.util.pattern.Cleanable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Range

/**
 * This interface represents renderer of models.
 */
interface ModelHandler : Cleanable {

    /**
     * Drawer of model handler.
     */
    val drawer: Drawer

    /**
     * Model, that this handler will render.
     */
    val model: Model

    /**
     * Bounding box of this model.
     */
    val aabb: AABB

    /**
     * Tick animation. Call it before [render].
     *
     * @param delta Delta tick for animating.
     */
    fun tick(delta: Float)

    /**
     * Render this model via specific shader.
     *
     * @param shader Shader for rendering.
     */
    fun render(shader: ShaderInstance)

    /**
     * Play some animation in model.
     *
     * @param name Name of animation to play.
     */
    fun playAnimation(name: String)

    /**
     * Force stop any animation.
     *
     * @param name Name of animation to stop.
     */
    fun stopAnimation(name: String)

    /**
     * Rotate model.
     *
     * @param x X degrees.
     * @param y Y degrees.
     * @param z Z degrees.
     */
    fun rotate(x: Float = 0f, y: Float = 0f, z: Float = 0f)

    /**
     * Set scaling of model.
     *
     * @param x X scale.
     * @param y Y scale.
     * @param z Z scale.
     */
    fun scale(
        x: @Range(from = 0, to = 1) Float = 1f,
        y: @Range(from = 0, to = 1) Float = x,
        z: @Range(from = 0, to = 1) Float = y
    )

    /**
     * Set position of model in 3D space.
     *
     * @param x X position.
     * @param y Y position.
     * @param z Z position.
     */
    fun position(x: Float, y: Float, z: Float)

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(drawer: Drawer, model: Model): ModelHandler

    }

    companion object {

        /**
         * Wrap model with render utils. It allows render it.
         *
         * @param drawer Drawer for rendering model.
         * @param model Model to wrap.
         *
         * @return New instance of [ModelHandler].
         */
        @JvmStatic
        fun of(
            drawer: Drawer = Application.find().renderSystem.drawer,
            model: Model
        ): ModelHandler {
            return single<Factory>().create(drawer, model)
        }

    }

}