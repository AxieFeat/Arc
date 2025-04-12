package arc.graphics

import arc.culling.ArcFrustum
import arc.culling.Frustum
import arc.math.Point3d
import arc.math.Ray
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

internal data class ArcCamera(
    override var fov: Float,
    override var windowWidth: Float,
    override var windowHeight: Float,
) : Camera {

    override val ray: Ray = Ray.ZERO.copy()

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

        val originVec = Vector3f(position.x.toFloat(), position.y.toFloat(), position.z.toFloat())

        val forward = Vector3f(0f, 0f, -1f).rotate(rotation).normalize()

        val target = Vector3f(originVec).add(forward)
        val up = Vector3f(0f, 1f, 0f).rotate(rotation)

        view.identity().lookAt(originVec, target, up)
        combined.set(projection).mul(view)

        ray.origin.apply {
            x = originVec.x
            y = originVec.y
            z = originVec.z
        }
        ray.direction.apply {
            x = forward.x
            y = forward.y
            z = forward.z
        }
    }

    override fun updateAspect(width: Int, height: Int) {
        if (windowHeight == height.toFloat() && windowWidth == width.toFloat()) return

        windowHeight = height.toFloat()
        windowWidth = width.toFloat()
        aspect = windowWidth / windowHeight
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