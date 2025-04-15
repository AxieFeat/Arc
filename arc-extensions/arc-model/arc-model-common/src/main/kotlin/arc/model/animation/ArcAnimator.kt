package arc.model.animation

import java.util.*

internal data class ArcAnimator(
    override val uuid: UUID = UUID.randomUUID(),
    override val target: String = "",
    override val keyframes: Set<Keyframe> = setOf()
) : Animator {

    object Factory : Animator.Factory {
        override fun create(uuid: UUID, target: String, keyframes: Set<Keyframe>): Animator {
            return ArcAnimator(uuid, target, keyframes)
        }
    }

}