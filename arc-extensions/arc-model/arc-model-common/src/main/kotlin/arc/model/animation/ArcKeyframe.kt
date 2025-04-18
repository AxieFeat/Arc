package arc.model.animation

import arc.math.Vec3f
import arc.util.InterpolationMode

internal data class ArcKeyframe(
    override val channel: AnimationChannel = AnimationChannel.POSITION,
    override val interpolation: InterpolationMode = InterpolationMode.LINEAR,
    override val time: Float = 1f,
    override val dataPoints: Vec3f = Vec3f.of(0f, 0f, 0f)
) : Keyframe {

    override fun copy(): Keyframe {
        return ArcKeyframe(channel, interpolation, time, dataPoints.copy())
    }

    class Builder : Keyframe.Builder {

        private var channel = AnimationChannel.POSITION
        private var interpolation = InterpolationMode.LINEAR
        private var time = 0f
        private var dataPoints = Vec3f.of(0f, 0f, 0f)

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
            this.dataPoints.apply {
                this.x = x
                this.y = y
                this.z = z
            }

            return this
        }

        override fun build(): Keyframe {
            return ArcKeyframe(channel, interpolation, time, dataPoints)
        }

    }

}