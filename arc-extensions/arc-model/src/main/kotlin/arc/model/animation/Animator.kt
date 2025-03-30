package arc.model.animation

import arc.annotations.ImmutableType
import arc.util.Identifiable

/**
 * This interface represents animator of animation.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Animator : Identifiable {

    /**
     * Name of target group of elements.
     */
    @get:JvmName("target")
    val target: String

    /**
     * Keyframes of this animator.
     */
    @get:JvmName("keyframes")
    val keyframes: Set<Keyframe>

}