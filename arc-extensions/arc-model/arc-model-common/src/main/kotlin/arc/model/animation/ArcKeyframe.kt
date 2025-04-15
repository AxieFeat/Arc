package arc.model.animation

import arc.math.Point3d
import arc.util.InterpolationMode
import java.util.*

internal data class ArcKeyframe(
    override val uuid: UUID = UUID.randomUUID(),
    override val channel: AnimationChannel = AnimationChannel.SCALE,
    override val time: Double = 0.0,
    override val dataPoints: Point3d = Point3d.ZERO,
    override val interpolation: InterpolationMode = InterpolationMode.LINEAR
) : Keyframe {

    object Factory : Keyframe.Factory {
        override fun create(
            uuid: UUID,
            channel: AnimationChannel,
            time: Double,
            dataPoints: Point3d,
            interpolation: InterpolationMode
        ): Keyframe {
            return ArcKeyframe(uuid, channel, time, dataPoints, interpolation)
        }
    }

}