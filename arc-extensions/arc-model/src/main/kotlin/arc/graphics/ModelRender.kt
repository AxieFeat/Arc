package arc.graphics

import arc.Arc
import arc.annotations.TypeFactory
import arc.model.Model
import arc.shader.ShaderInstance
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Range

/**
 * This interface represents renderer of models.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface ModelRender {

    /**
     * Model, that this renderer will render.
     */
    @get:JvmName("model")
    val model: Model

    /**
     * Tick animation. Call it before [render].
     *
     * @param partialTick Partial tick for animating.
     */
    fun tick(partialTick: Float)

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
        y: @Range(from = 0, to = 1) Float = 1f,
        z: @Range(from = 0, to = 1) Float = 1f
    )

    /**
     * Set position of model in 3D space.
     *
     * @param x X position.
     * @param y Y position.
     * @param z Z position.
     */
    fun position(x: Float, y: Float, z: Float)

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(model: Model): ModelRender

    }

    companion object {

        /**
         * Wrap model with render utils. It allows render it.
         *
         * @param model Model to wrap.
         *
         * @return New instance of [ModelRender].
         */
        @JvmStatic
        fun of(model: Model): ModelRender {
            return Arc.factory<Factory>().create(model)
        }

    }

}