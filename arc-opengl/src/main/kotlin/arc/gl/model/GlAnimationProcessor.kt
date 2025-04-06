package arc.gl.model

import arc.model.animation.Animation
import org.joml.Matrix4f

internal data class GlAnimationProcessor(
    val matrix: Matrix4f,
    val animation: Animation,
) {

    private var animators = calculateAnimators()

    fun update(partial: Float): Boolean {
        animators.removeIf { it.update(partial) }

        return animators.isEmpty()
    }

    fun reset(): GlAnimationProcessor {
        animators = calculateAnimators()

        return this
    }

    private fun calculateAnimators(): MutableList<GlAnimatorProcessor> {
        val result = mutableListOf<GlAnimatorProcessor>()

        animation.animators.forEach { animator ->
            result.add(GlAnimatorProcessor(matrix, animator))
        }

        return result
    }

}