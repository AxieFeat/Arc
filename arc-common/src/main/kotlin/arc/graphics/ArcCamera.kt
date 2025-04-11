package arc.graphics

import arc.culling.ArcFrustum
import arc.culling.Frustum
import arc.math.Point3d
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

internal data class ArcCamera(
    override var fov: Float,
    override var windowWidth: Float,
    override var windowHeight: Float,
) : Camera {

    override var view: Matrix4f = Matrix4f()
    override var projection: Matrix4f = Matrix4f()
    override val combined: Matrix4f = Matrix4f()

    override var position: Point3d = Point3d.ZERO
    override var rotation: Quaternionf = Quaternionf()
    override var zNear: Float = 0.1f
    override var zFar: Float = 100f

    private var lookAtTarget: Point3d? = null
    private var aspect = windowWidth / windowHeight

    override val frustum: Frustum = ArcFrustum(this)

    override fun lookAt(target: Point3d) {
        lookAtTarget = target
    }

    override fun rotate(yaw: Float, pitch: Float, roll: Float) {
        rotation.rotateXYZ(
            Math.toRadians(yaw),
            Math.toRadians(pitch),
            Math.toRadians(roll)
        )
    }

    override fun update() {
        projection.identity()
            .perspective(Math.toRadians(fov), aspect, zNear, zFar)

        view.identity()

        val up = Vector3f(0f, 1f, 0f).rotate(rotation)

        if (lookAtTarget != null) {
            val direction = Vector3f(
                position.x.toFloat() - lookAtTarget!!.x.toFloat(),
                position.y.toFloat() - lookAtTarget!!.y.toFloat(),
                position.z.toFloat() - lookAtTarget!!.z.toFloat()
            ).rotate(rotation)

            val newPosition = Vector3f(lookAtTarget!!.x.toFloat(), lookAtTarget!!.y.toFloat(), lookAtTarget!!.z.toFloat()).add(direction)

            view.lookAt(newPosition, Vector3f(lookAtTarget!!.x.toFloat(), lookAtTarget!!.y.toFloat(), lookAtTarget!!.z.toFloat()), up)
        } else {
            val front = Vector3f(0f, 0f, -1f).rotate(rotation)

            val target = Vector3f(
                position.x.toFloat() + front.x,
                position.y.toFloat() + front.y,
                position.z.toFloat() + front.z
            )

            view.lookAt(
                Vector3f(position.x.toFloat(), position.y.toFloat(), position.z.toFloat()),
                target,
                up
            )
        }

        combined.set(projection).mul(view)
    }

    override fun updateAspect(width: Int, height: Int) {
        if(windowHeight == height.toFloat() && windowWidth == width.toFloat()) return

        windowHeight = height.toFloat()
        windowWidth = width.toFloat()
        aspect = windowWidth / windowHeight
    }

    override fun resetLookAt() {
        lookAtTarget = null
    }

    override fun reset() {
        view.identity()
        projection.identity()
        combined.set(projection).mul(view)
        lookAtTarget = null
        rotation.identity()
        position = Point3d.ZERO
    }

    object Factory : Camera.Factory {
        override fun create(fov: Float, width: Float, height: Float): Camera {
            return ArcCamera(fov, width, height)
        }
    }
}