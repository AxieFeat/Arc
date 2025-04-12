package arc.model.animation

import arc.annotations.ImmutableType
import arc.math.Point3d
import arc.util.pattern.Identifiable
import arc.util.InterpolationMode

/**
 * This interface represents keyframe of animation
 */
@ImmutableType
interface Keyframe : Identifiable {

    /**
     * Channel of this keyframe.
     */
    val channel: AnimationChannel

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

}
