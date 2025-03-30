package arc.lwamodel.animation

import arc.Arc
import arc.annotations.TypeFactory
import arc.model.animation.Animation
import arc.model.animation.AnimationLoopMode
import arc.model.animation.Animator
import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * This interface represents animation in LWAM format.
 */
interface LwamAnimation : Animation {

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(
            uuid: UUID,
            loop: AnimationLoopMode,
            startDelay: Long,
            loopDelay: Long,
            duration: Long,
            animators: Set<Animator>,
        ): LwamAnimation

    }

    companion object {

        /**
         * Create new instance of [LwamAnimation].
         *
         * @param uuid Unique id of animation.
         * @param loop Loop mode of animation.
         * @param startDelay Start delay of animation in milliseconds.
         * @param loopDelay Loop delay of animation in milliseconds.
         * @param duration Duration of animation in milliseconds.
         * @param animators Animators of animation in milliseconds.
         */
        @JvmStatic
        fun of(
            uuid: UUID = UUID.randomUUID(),
            loop: AnimationLoopMode = AnimationLoopMode.PLAY_ONCE,
            startDelay: Long = 0,
            loopDelay: Long = 0,
            duration: Long = 0,
            animators: Set<Animator> = setOf(),
        ): LwamAnimation {
            return Arc.factory<Factory>().create(uuid, loop, startDelay, loopDelay, duration, animators)
        }

    }

}