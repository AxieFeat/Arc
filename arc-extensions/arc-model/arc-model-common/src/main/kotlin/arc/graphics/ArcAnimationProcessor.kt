package arc.graphics

import arc.model.animation.Animation
import org.joml.Matrix4f

internal data class ArcAnimationProcessor(
    val matrix: Matrix4f,
    val animation: Animation,
) {

    private var animators = calculateAnimators()

    fun update(delta: Float): Boolean {
        animators.removeIf { it.update(delta) }

        return animators.isEmpty()
    }

    fun reset(): ArcAnimationProcessor {
        animators = calculateAnimators()

        return this
    }

    private fun calculateAnimators(): MutableList<ArcAnimatorProcessor> {
        val result = mutableListOf<ArcAnimatorProcessor>()

        animation.animators.forEach { animator ->
            result.add(ArcAnimatorProcessor(matrix, animator))
        }

        return result
    }

}