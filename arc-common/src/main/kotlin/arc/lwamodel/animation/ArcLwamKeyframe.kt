package arc.lwamodel.animation

import arc.math.Point3d
import arc.model.animation.AnimationChannel
import arc.util.InterpolationMode
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ArcLwamKeyframe(
    @Contextual
    override val uuid: UUID = UUID.randomUUID(),
    override val channel: AnimationChannel = AnimationChannel.SCALE,
    override val time: Double = 0.0,
    override val dataPoints: Point3d = Point3d.ZERO,
    override val interpolation: InterpolationMode = InterpolationMode.LINEAR
) : LwamKeyframe {

    object Factory : LwamKeyframe.Factory {
        override fun create(
            uuid: UUID,
            channel: AnimationChannel,
            time: Double,
            dataPoints: Point3d,
            interpolation: InterpolationMode
        ): LwamKeyframe {
            return ArcLwamKeyframe(uuid, channel, time, dataPoints, interpolation)
        }
    }

}