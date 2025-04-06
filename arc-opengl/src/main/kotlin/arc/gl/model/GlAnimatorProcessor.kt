package arc.gl.model

import arc.model.animation.Animator
import org.joml.Matrix4f

internal data class GlAnimatorProcessor(
    val matrix: Matrix4f,
    val animator: Animator,
) {

    private var keyframes = calculateKeyframes()

    fun update(partial: Float): Boolean {
        keyframes.removeIf { it.update(partial) }

        return keyframes.isEmpty()
    }

    fun reset(): GlAnimatorProcessor {
        keyframes = calculateKeyframes()

        return this
    }

    private fun calculateKeyframes(): MutableList<GlKeyframeProcessor> {
        val result = mutableListOf<GlKeyframeProcessor>()

        animator.keyframes.sortedBy { it.time }.forEach { keyframe ->
            result.add(
                GlKeyframeProcessor(
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