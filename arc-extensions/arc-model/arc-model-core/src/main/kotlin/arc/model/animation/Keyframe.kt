package arc.model.animation

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.math.Point3d
import arc.util.pattern.Identifiable
import arc.util.InterpolationMode
import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * This interface represents keyframe of animation
 */
@ImmutableType
interface Keyframe : Identifiable {

    /**
     * Channel of this keyframe.
     */
    val channel: arc.model.animation.AnimationChannel

    /**
     * Time after animation start when this frame will be executed in seconds.
     */
    val time: Double

    /**
     * Points for changing elements.
     */
    val dataPoints: Point3d

    /**
     * Mode of interpolation for elements changing.
     */
    val interpolation: InterpolationMode

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(
            uuid: UUID,
            channel: arc.model.animation.AnimationChannel,
            time: Double,
            dataPoints: Point3d,
            interpolation: InterpolationMode
        ): arc.model.animation.Keyframe

    }

    companion object {

        /**
         * Create new instance of [Keyframe].
         *
         * @param uuid Unique id of keyframe.
         * @param channel Channel of keyframe.
         * @param time Time of keyframe.
         * @param dataPoints Data points of keyframe.
         * @param interpolation Interpolation mode of keyframe.
         *
         * @return New instance of [Keyframe].
         */
        @JvmStatic
        fun of(
            uuid: UUID = UUID.randomUUID(),
            channel: arc.model.animation.AnimationChannel = arc.model.animation.AnimationChannel.SCALE,
            time: Double = 0.0,
            dataPoints: Point3d = Point3d.ZERO,
            interpolation: InterpolationMode = InterpolationMode.LINEAR
        ): arc.model.animation.Keyframe {
            return Arc.factory<arc.model.animation.Keyframe.Factory>().create(uuid, channel, time, dataPoints, interpolation)
        }

    }

}
