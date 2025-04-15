package arc.model.animation

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.util.pattern.Identifiable
import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * This interface represents animator of animation.
 */
@ImmutableType
interface Animator : Identifiable {

    /**
     * Name of target group of elements.
     */
    val target: String

    /**
     * Keyframes of this animator.
     */
    val keyframes: Set<arc.model.animation.Keyframe>

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(uuid: UUID, target: String, keyframes: Set<arc.model.animation.Keyframe>): arc.model.animation.Animator

    }

    companion object {

        /**
         * Create new instance of [Animator].
         *
         * @param uuid Unique id of animator.
         * @param target Target group of animator.
         * @param keyframes Keyframes of animator.
         *
         * @return New instance of [Animator].
         */
        @JvmStatic
        fun of(
            uuid: UUID = UUID.randomUUID(),
            target: String = "",
            keyframes: Set<arc.model.animation.Keyframe> = setOf()
        ): arc.model.animation.Animator {
            return Arc.factory<arc.model.animation.Animator.Factory>().create(uuid, target, keyframes)
        }

    }

}