package arc.model.animation

import arc.Arc
import arc.annotations.ImmutableType
import arc.util.InterpolationMode
import arc.util.pattern.Copyable
import org.jetbrains.annotations.ApiStatus
import org.joml.Vector3f

/**
 * This interface represents keyframe of animation
 */
@ImmutableType
interface Keyframe : Copyable<Keyframe> {

    /**
     * Channel of this keyframe.
     */
    val channel: AnimationChannel

    /**
     * Mode of interpolation for elements changing.
     */
    val interpolation: InterpolationMode

    /**
     * Time after animation start when this frame will be executed in seconds.
     */
    val time: Float

    /**
     * Points for changing elements.
     */
    val dataPoints: Vector3f

    @ApiStatus.Internal
    interface Builder : arc.util.pattern.Builder<Keyframe> {

        fun setChannel(channel: AnimationChannel): Builder
        fun setInterpolation(interpolation: InterpolationMode): Builder
        fun setTime(time: Float): Builder

        fun setDataPoints(dataPoints: Vector3f): Builder = setDataPoints(dataPoints.x, dataPoints.y, dataPoints.z)
        fun setDataPoints(x: Float, y: Float, z: Float): Builder

    }

    companion object {

        /**
         * Create new instance of [Keyframe] via builder.
         *
         * @return New instance of [Builder].
         */
        @JvmStatic
        fun builder(): Builder {
            return Arc.factory<Builder>()
        }

    }

}
