package arc.input.mouse

import arc.display.Display
import arc.graphics.Camera
import arc.input.KeyCode
import org.joml.Vector2f

internal data class SimpleVirtualMouse(
    override val display: Display
) : VirtualMouseInput {

    override var previousPosition: Vector2f = Vector2f()
    override var position: Vector2f = Vector2f()
    override var displayVec: Vector2f = Vector2f()
    override fun reset() {

    }

    override fun update(camera: Camera) {
//        previousPosition = Point2d.of(position.x, position.y)
//
//        val ray = camera.ray
//        val origin = ray.origin
//        val direction = ray.direction
//
//        val aabb = display.aabb
//
//        val min = aabb.min
//        val max = aabb.max
//
//        val planeCenter = Vec3f.of(
//            (min.x + max.x) / 2f,
//            (min.y + max.y) / 2f,
//            (min.z + max.z) / 2f
//        )
//        val planeNormal = Vec3f.of(0f, 0f, 1f)
//
//        val diff = Vec3f.of(0f, 0f, 0f).set(planeCenter).sub(origin)
//        val denom = direction.dot(planeNormal)
//
//        if (kotlin.math.abs(denom) > 1e-6f) {
//            val t = diff.dot(planeNormal) / denom
//
//            if (t >= 0f) {
//                val hit = Vec3f.of(0f, 0f, 0f).set(direction).scl(t).add(origin)
//
//                if (
//                    hit.x in min.x..max.x &&
//                    hit.y in min.y..max.y &&
//                    hit.z in min.z..max.z
//                ) {
//                    val u = (hit.x - min.x) / (max.x - min.x)
//                    val v = (hit.y - min.y) / (max.y - min.y)
//
//                    val displayWidth = max.x - min.x
//                    val displayHeight = max.y - min.y
//
//                    position = Point2d.of((u * displayWidth).toDouble(), (v * displayHeight).toDouble())
//                } else {
//                    position = Point2d.of(-1.0, -1.0)
//                }
//            } else {
//                position = Point2d.of(-1.0, -1.0)
//            }
//        } else {
//            position = Point2d.of(-1.0, -1.0)
//        }
//
//        displayVec.x = (position.y - previousPosition.y).toFloat()
//        displayVec.y = (position.x - previousPosition.x).toFloat()
    }


    override fun isPressed(key: KeyCode): Boolean {
       return false
    }

    override fun isReleased(key: KeyCode): Boolean {
        return false
    }

    object Factory : VirtualMouseInput.Factory {
        override fun create(display: Display): VirtualMouseInput {
            return SimpleVirtualMouse(display)
        }
    }

}