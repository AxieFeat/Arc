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
     * Name of animation. It used for control it.
     */
    @get:JvmName("name")
    val name: String

    /**
     * Mode of animation looping.
     */
    @get:JvmName("loop")
    val loop: AnimationLoopMode

    /**
     * Delay for starting this animation in seconds.
     */
    @get:JvmName("startDelay")
    val startDelay: Double

    /**
     * Delay for looping this animation in seconds.
     */
    @get:JvmName("loopDelay")
    val loopDelay: Double

    /**
     * Duration of this animation in seconds.
     */
    @get:JvmName("duration")
    val duration: Double

    /**
     * All animators of this animation.
     */
    @get:JvmName("animators")
    val animators: Set<Animator>

}