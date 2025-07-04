package arc.model.animation

import arc.util.InterpolationMode
import org.joml.Vector3f

internal data class SimpleKeyframe(
    override val channel: AnimationChannel = AnimationChannel.POSITION,
    override val interpolation: InterpolationMode = InterpolationMode.LINEAR,
    override val time: Float = 1f,
    override var dataPoints: Vector3f = Vector3f(0f, 0f, 0f)
) : Keyframe {

    override fun copy(): Keyframe {
        return SimpleKeyframe(channel, interpolation, time, Vector3f(dataPoints))
    }

    class Builder : Keyframe.Builder {

        private var channel = AnimationChannel.POSITION
        private var interpolation = InterpolationMode.LINEAR
        private var time = 0f
        private var dataPoints = Vector3f(0f, 0f, 0f)

        override fun setChannel(channel: AnimationChannel): Keyframe.Builder {
            this.channel = channel

            return this
        }

        override fun setInterpolation(interpolation: InterpolationMode): Keyframe.Builder {
            this.interpolation = interpolation

            return this
        }

        override fun setTime(time: Float): Keyframe.Builder {
            this.time = time

            return this
        }

        override fun setDataPoints(x: Float, y: Float, z: Float): Keyframe.Builder {
            dataPoints.set(x, y, z)

            return this
        }

        override fun build(): Keyframe {
            return SimpleKeyframe(channel, interpolation, time, dataPoints)
        }

    }

}