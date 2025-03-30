package arc.model.animation

import arc.annotations.ImmutableType
import arc.util.Identifiable

/**
 * This interface represents animation of some model.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Animation : Identifiable {

    /**
     * Mode of animation looping
     */
    @get:JvmName("loop")
    val loop: AnimationLoopMode

    /**
     * Delay for starting this animation in milliseconds.
     */
    @get:JvmName("startDelay")
    val startDelay: Long

    /**
     * Delay for looping this animation in milliseconds.
     */
    @get:JvmName("loopDelay")
    val loopDelay: Long

    /**
     * Duration of this animation in milliseconds.
     */
    @get:JvmName("duration")
    val duration: Long

    /**
     * All animators of this animation.
     */
    @get:JvmName("animators")
    val animators: Set<Animator>

}