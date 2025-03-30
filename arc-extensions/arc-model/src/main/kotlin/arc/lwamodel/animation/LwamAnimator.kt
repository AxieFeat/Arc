package arc.lwamodel.animation

import arc.Arc
import arc.annotations.TypeFactory
import arc.model.animation.Animator
import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * This interface represents animator in LWAM format.
 */
interface LwamAnimator : Animator {

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(uuid: UUID, target: String, keyframes: Set<LwamKeyframe>): LwamAnimator

    }

    companion object {

        /**
         * Create new instance of [LwamAnimator].
         *
         * @param uuid Unique id of animator.
         * @param target Target group of animator.
         * @param keyframes Keyframes of animator.
         *
         * @return New instance of [LwamAnimator].
         */
        @JvmStatic
        fun of(
            uuid: UUID = UUID.randomUUID(),
            target: String = "",
            keyframes: Set<LwamKeyframe> = setOf()
        ): LwamAnimator {
            return Arc.factory<Factory>().create(uuid, target, keyframes)
        }

    }

}