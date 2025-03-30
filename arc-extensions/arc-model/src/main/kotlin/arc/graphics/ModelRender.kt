package arc.graphics

import arc.Arc
import arc.annotations.TypeFactory
import arc.math.Point3d
import arc.model.Model
import arc.shader.ShaderInstance
import org.jetbrains.annotations.ApiStatus
import org.joml.Quaternionf

/**
 * This interface represents renderer of models.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface ModelRender {

    /**
     * Model, that this renderer render.
     */
    @get:JvmName("model")
    val model: Model

    /**
     * Position of model in 3D space.
     */
    @get:JvmName("position")
    var position: Point3d

    /**
     * Rotation of model in 3D space.
     */
    @get:JvmName("rotation")
    var rotation: Quaternionf

    /**
     * Scale of model.
     */
    @get:JvmName("scale")
    var scale: Float

    /**
     * Tick animation. Call it before [render].
     *
     * @param delta Delta for animation.
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