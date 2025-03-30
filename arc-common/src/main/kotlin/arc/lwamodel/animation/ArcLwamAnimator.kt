package arc.lwamodel.animation

import arc.model.animation.Keyframe
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ArcLwamAnimator(
    @Contextual
    override val uuid: UUID = UUID.randomUUID(),
    override val target: String = "",
    override val keyframes: Set<Keyframe> = setOf()
) : LwamAnimator {

    object Factory : LwamAnimator.Factory {
        override fun create(uuid: UUID, target: String, keyframes: Set<LwamKeyframe>): LwamAnimator {
            return ArcLwamAnimator(uuid, target, keyframes)
        }
    }

}