package arc.graphics

import arc.math.Point3d
import arc.model.animation.AnimationChannel
import arc.model.animation.Keyframe
import org.joml.Matrix4f
import kotlin.math.min

internal data class ArcKeyframeProcessor(
    val matrix: Matrix4f,
    val keyframe: Keyframe,
    val duration: Float,
    val easing: (Float) -> Float,
) {

    private var elapsedTime: Float = 0.0f

//    private var rotation = Point3d.ZERO.copy()
//    private var translation = Point3d.ZERO.copy()
//    private var scale = Point3d.of(1.0, 1.0, 1.0)

    fun update(delta: Float): Boolean {
        elapsedTime += delta

        val progress = if(duration <= 0.0f) 1.0f else min(elapsedTime / duration, 1.0f)
        if(interpolate(easing(progress)).also { updateMatrix() }) {
            return true
        }

        return progress >= 1.0f
    }

    fun reset(): ArcKeyframeProcessor {
        elapsedTime = 0f
        matrix.identity()
//        rotation = Point3d.ZERO.copy()
//        translation = Point3d.ZERO.copy()
//        scale = Point3d.of(1.0, 1.0, 1.0)

        return this
    }

    private fun interpolate(progress: Float): Boolean {
//        when(keyframe.channel) {
//            AnimationChannel.ROTATION -> {
//                rotation.interpolate(keyframe.dataPoints, progress)
//
//                if(rotation == keyframe.dataPoints) return true
//            }
//            AnimationChannel.POSITION -> {
//                translation.interpolate(keyframe.dataPoints, progress)
//
//                if(translation == keyframe.dataPoints) return true
//            }
//            AnimationChannel.SCALE -> {
//                scale.interpolate(keyframe.dataPoints, progress)
//
//                if(scale == keyframe.dataPoints) return true
//            }
//        }

        return false
    }

    private fun updateMatrix() {
//        when(keyframe.channel) {
//            AnimationChannel.ROTATION -> {
//                matrix
//                    .setRotationXYZ(rotation.x.toFloat(), rotation.y.toFloat(), rotation.z.toFloat())
//            }
//            AnimationChannel.POSITION -> {
//                matrix
//                    .translation(translation.x.toFloat(), translation.y.toFloat(), translation.z.toFloat())
//            }
//            AnimationChannel.SCALE -> {
//                matrix
//                    .scale(scale.x.toFloat(), scale.y.toFloat(), scale.z.toFloat())
//            }
//        }
    }
}