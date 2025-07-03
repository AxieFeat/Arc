package arc.graphics

import arc.model.animation.Animator
import org.joml.Matrix4f

internal data class SimpleAnimatorProcessor(
    val matrix: Matrix4f,
    val animator: Animator,
) {

    private var keyframes = calculateKeyframes()

    fun update(delta: Float): Boolean {
        keyframes.removeIf { it.update(delta) }

        return keyframes.isEmpty()
    }

    fun reset(): SimpleAnimatorProcessor {
        keyframes = calculateKeyframes()

        return this
    }

    private fun calculateKeyframes(): MutableList<SimpleKeyframeProcessor> {
        val result = mutableListOf<SimpleKeyframeProcessor>()

        animator.keyframes.sortedBy { it.time }.forEach { keyframe ->
            result.add(
                SimpleKeyframeProcessor(
                    matrix = matrix,
                    keyframe = keyframe,
                    duration = keyframe.time.toFloat(),
                    easing = { it }
                )
            )
        }

        return result
    }

}