package arc.model.animation

import arc.annotations.ImmutableType
import arc.util.pattern.Identifiable

/**
 * This interface represents animator of animation.
 */
@ImmutableType
interface Animator : Identifiable {

    /**
     * Name of target group of elements.
     */
    val target: String

    /**
     * Keyframes of this animator.
     */
    val keyframes: Set<Keyframe>

}