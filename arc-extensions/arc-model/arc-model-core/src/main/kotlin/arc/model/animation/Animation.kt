package arc.model.animation

import arc.Arc.factory
import arc.annotations.ImmutableType
import arc.util.pattern.Copyable
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents animation of some model.
 */
@ImmutableType
interface Animation : Copyable<Animation> {

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
    val startDelay: Float

    /**
     * Delay for looping this animation in seconds.
     */
    val loopDelay: Float

    /**
     * Duration of this animation in seconds.
     */
    val duration: Float

    /**
     * All animators of this animation.
     */
    val animators: List<Animator>

    @ApiStatus.Internal
    interface Builder : arc.util.pattern.Builder<Animation> {

        fun setName(name: String): Builder
        fun setLoop(loop: AnimationLoopMode): Builder
        fun setStartDelay(startDelay: Float): Builder
        fun setLoopDelay(loopDelay: Float): Builder
        fun setDuration(duration: Float): Builder
        fun addAnimator(vararg animator: Animator): Builder

    }

    companion object {

        /**
         * Create new instance of [Animation] via builder.
         *
         * @return New instance of [Builder].
         */
        @JvmStatic
        fun builder(): Builder {
            return factory<Builder>()
        }

    }

}