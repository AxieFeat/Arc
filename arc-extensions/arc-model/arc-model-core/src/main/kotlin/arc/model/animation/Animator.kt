package arc.model.animation

import arc.Arc.factory
import arc.annotations.ImmutableType
import arc.model.group.ElementGroup
import arc.util.pattern.Copyable
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents animator of animation.
 */
@ImmutableType
interface Animator : Copyable<Animator> {

    /**
     * Name of target group of elements.
     */
    val target: String

    /**
     * Keyframes of this animator.
     */
    val keyframes: Set<Keyframe>

    @ApiStatus.Internal
    interface Builder : arc.util.pattern.Builder<Animator> {

        fun setTarget(group: ElementGroup): Builder = setTarget(group.name)
        fun setTarget(target: String): Builder

        fun addKeyframe(vararg keyframe: Keyframe): Builder
    }

    companion object {

        /**
         * Create new instance of [Animator] via builder.
         *
         * @return New instance of [Builder].
         */
        @JvmStatic
        fun builder(): Builder {
            return factory<Builder>()
        }
    }
}
