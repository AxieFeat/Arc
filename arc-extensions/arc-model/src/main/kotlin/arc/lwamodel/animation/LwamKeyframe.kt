package arc.lwamodel.animation

import arc.Arc
import arc.annotations.TypeFactory
import arc.math.Point3d
import arc.model.animation.AnimationChannel
import arc.model.animation.Keyframe
import arc.util.InterpolationMode
import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * This interface represents keyframe in LWAM format.
 */
interface LwamKeyframe : Keyframe {

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(
            uuid: UUID,
            channel: AnimationChannel,
            time: Long,
            dataPoints: Point3d,
            interpolation: InterpolationMode
        ): LwamKeyframe

    }

    companion object {

        /**
         * Create new instance of [LwamKeyframe].
         *
         * @param uuid Unique id of keyframe.
         * @param channel Channel of keyframe.
         * @param time Time of keyframe.
         * @param dataPoints Data points of keyframe.
         * @param interpolation Interpolation mode of keyframe.
         *
         * @return New instance of [LwamKeyframe].
         */
        @JvmStatic
        fun of(
            uuid: UUID = UUID.randomUUID(),
            channel: AnimationChannel = AnimationChannel.SCALE,
            time: Long = 0,
            dataPoints: Point3d = Point3d.ZERO,
            interpolation: InterpolationMode = InterpolationMode.LINEAR
        ): LwamKeyframe {
            return Arc.factory<Factory>().create(uuid, channel, time, dataPoints, interpolation)
        }

    }

}