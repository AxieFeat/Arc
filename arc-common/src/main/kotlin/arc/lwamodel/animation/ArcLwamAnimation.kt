package arc.lwamodel.animation

import arc.model.animation.AnimationLoopMode
import arc.model.animation.Animator
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
internal data class ArcLwamAnimation(
    override val name: String,
    @Contextual
    override val uuid: UUID = UUID.randomUUID(),
    override val loop: AnimationLoopMode = AnimationLoopMode.PLAY_ONCE,
    override val startDelay: Long = 0,
    override val loopDelay: Long = 0,
    override val duration: Long = 0,
    override val animators: Set<Animator> = setOf(),
) : LwamAnimation {

    object Factory : LwamAnimation.Factory {
        override fun create(
            name: String,
            uuid: UUID,
            loop: AnimationLoopMode,
            startDelay: Long,
            loopDelay: Long,
            duration: Long,
            animators: Set<Animator>
        ): LwamAnimation {
            return ArcLwamAnimation(name, uuid, loop, startDelay, loopDelay, duration, animators)
        }

    }

}