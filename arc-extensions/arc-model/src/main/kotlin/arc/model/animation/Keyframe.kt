package arc.model.animation

import arc.annotations.ImmutableType
import arc.math.Point3d
import arc.util.pattern.Identifiable
import arc.util.InterpolationMode

/**
 * This interface represents keyframe of animation
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Keyframe : Identifiable {

    /**
     * Channel of this keyframe.
     */
    @get:JvmName("channel")
    val channel: AnimationChannel

    /**
     * Time after animation start when this frame will be executed in seconds.
     */
    @get:JvmName("duration")
    val time: Double

    /**
     * Points for changing elements.
     */
    @get:JvmName("dataPoints")
    val dataPoints: Point3d

    /**
     * Mode of interpolation for elements changing.
     */
    @get:JvmName("interpolation")
    val interpolation: InterpolationMode

}
