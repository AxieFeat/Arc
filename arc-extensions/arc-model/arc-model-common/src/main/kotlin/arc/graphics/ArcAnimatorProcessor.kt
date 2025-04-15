package arc.graphics

import arc.model.animation.Animator
import org.joml.Matrix4f

internal data class ArcAnimatorProcessor(
    val matrix: Matrix4f,
    val animator: Animator,
) {

    private var keyframes = calculateKeyframes()

    fun update(delta: Float): Boolean {
        keyframes.removeIf { it.update(delta) }

        return keyframes.isEmpty()
    }

    fun reset(): ArcAnimatorProcessor {
        keyframes = calculateKeyframes()

        return this
    }

    private fun calculateKeyframes(): MutableList<ArcKeyframeProcessor> {
        val result = mutableListOf<ArcKeyframeProcessor>()

        animator.keyframes.sortedBy { it.time }.forEach { keyframe ->
            result.add(
                ArcKeyframeProcessor(
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