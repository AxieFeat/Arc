package arc.graphics

import arc.model.animation.Animation
import org.joml.Matrix4f

internal data class SimpleAnimationProcessor(
    val matrix: Matrix4f,
    val animation: Animation,
) {

    private var animators = calculateAnimators()

    fun update(delta: Float): Boolean {
        animators.removeIf { it.update(delta) }

        return animators.isEmpty()
    }

    fun reset(): SimpleAnimationProcessor {
        animators = calculateAnimators()

        return this
    }

    private fun calculateAnimators(): MutableList<SimpleAnimatorProcessor> {
        val result = mutableListOf<SimpleAnimatorProcessor>()

        animation.animators.forEach { animator ->
            result.add(SimpleAnimatorProcessor(matrix, animator))
        }

        return result
    }

}