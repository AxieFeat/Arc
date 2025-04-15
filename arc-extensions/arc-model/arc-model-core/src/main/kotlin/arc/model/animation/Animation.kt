package arc.model.animation

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.util.pattern.Identifiable
import org.jetbrains.annotations.ApiStatus
import java.util.*

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

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(
            name: String,
            uuid: UUID,
            loop: AnimationLoopMode,
            startDelay: Double,
            loopDelay: Double,
            duration: Double,
            animators: Set<Animator>,
        ): Animation

    }

    companion object {

        /**
         * Create new instance of [Animation].
         *
         * @param name Name of animation.
         * @param uuid Unique id of animation.
         * @param loop Loop mode of animation.
         * @param startDelay Start delay of animation in milliseconds.
         * @param loopDelay Loop delay of animation in milliseconds.
         * @param duration Duration of animation in milliseconds.
         * @param animators Animators of animation in milliseconds.
         */
        @JvmStatic
        fun of(
            name: String = "",
            uuid: UUID = UUID.randomUUID(),
            loop: AnimationLoopMode = AnimationLoopMode.PLAY_ONCE,
            startDelay: Double = 0.0,
            loopDelay: Double = 0.0,
            duration: Double = 0.0,
            animators: Set<Animator> = setOf(),
        ): Animation {
            return Arc.factory<Factory>().create(name, uuid, loop, startDelay, loopDelay, duration, animators)
        }

    }

}