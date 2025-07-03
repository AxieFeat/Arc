package arc.model.animation

internal data class SimpleAnimator(
    override val target: String = "",
    override val keyframes: Set<Keyframe> = emptySet(),
) : Animator {

    override fun copy(): Animator {
        return SimpleAnimator(target, keyframes.map { it.copy() }.toSet())
    }

    class Builder : Animator.Builder {

        private var target: String = ""
        private var keyframes = mutableSetOf<Keyframe>()

        override fun setTarget(target: String): Animator.Builder {
            this.target = target

            return this
        }

        override fun addKeyframe(vararg keyframe: Keyframe): Animator.Builder {
            this.keyframes.addAll(keyframe)

            return this
        }

        override fun build(): Animator {
            return SimpleAnimator(target, keyframes)
        }

    }


}