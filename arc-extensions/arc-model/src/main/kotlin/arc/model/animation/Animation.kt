package arc.model.animation

import arc.annotations.ImmutableType
import arc.util.pattern.Identifiable

/**
 * This interface represents animation of some model.
 */
@ImmutableType
interface Animation : Identifiable {

    /**
     * Name of animation. It used for control it.
     */
    val name: String

    /**
     * Mode of animation looping.
     */
    val loop: AnimationLoopMode

    /**
     * Delay for starting this animation in seconds.
     */
    val startDelay: Double

    /**
     * Delay for looping this animation in seconds.
     */
    val loopDelay: Double

    /**
     * Duration of this animation in seconds.
     */
    val duration: Double

    /**
     * All animators of this animation.
     */
    val animators: Set<Animator>

}