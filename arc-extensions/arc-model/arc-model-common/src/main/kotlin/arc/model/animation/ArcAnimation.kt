package arc.model.animation

internal data class ArcAnimation(
    override val name: String = "",
    override val loop: AnimationLoopMode = AnimationLoopMode.LOOP,
    override val startDelay: Float = 0f,
    override val loopDelay: Float = 0f,
    override val duration: Float = 0f,
    override val animators: List<Animator> = emptyList(),
) : Animation {

    override fun copy(): Animation {
        return ArcAnimation(
            name,
            loop,
            startDelay,
            loopDelay,
            duration,
            animators.map { it.copy() }
        )
    }

    class Builder : Animation.Builder {

        private var name = ""
        private var loop = AnimationLoopMode.LOOP
        private var startDelay = 0f
        private var loopDelay = 0f
        private var duration = 0f
        private var animators = mutableListOf<Animator>()

        override fun setName(name: String): Animation.Builder {
            this.name = name

            return this
        }

        override fun setLoop(loop: AnimationLoopMode): Animation.Builder {
            this.loop = loop

            return this
        }

        override fun setStartDelay(startDelay: Float): Animation.Builder {
            this.startDelay = startDelay

            return this
        }

        override fun setLoopDelay(loopDelay: Float): Animation.Builder {
            this.loopDelay = loopDelay

            return this
        }

        override fun setDuration(duration: Float): Animation.Builder {
            this.duration = duration

            return this
        }

        override fun addAnimator(vararg animator: Animator): Animation.Builder {
            this.animators.addAll(animator)

            return this
        }

        override fun build(): Animation {
            return ArcAnimation(name, loop, startDelay, loopDelay, duration, animators)
        }

    }


}