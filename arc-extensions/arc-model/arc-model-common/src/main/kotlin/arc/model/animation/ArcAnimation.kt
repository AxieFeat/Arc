package arc.model.animation

import java.util.*

internal data class ArcAnimation(
    override val name: String,
    override val uuid: UUID = UUID.randomUUID(),
    override val loop: AnimationLoopMode = AnimationLoopMode.PLAY_ONCE,
    override val startDelay: Double = 0.0,
    override val loopDelay: Double = 0.0,
    override val duration: Double = 0.0,
    override val animators: Set<Animator> = setOf(),
) : Animation {

    object Factory : Animation.Factory {
        override fun create(
            name: String,
            uuid: UUID,
            loop: AnimationLoopMode,
            startDelay: Double,
            loopDelay: Double,
            duration: Double,
            animators: Set<Animator>
        ): Animation {
            return ArcAnimation(name, uuid, loop, startDelay, loopDelay, duration, animators)
        }

    }

}