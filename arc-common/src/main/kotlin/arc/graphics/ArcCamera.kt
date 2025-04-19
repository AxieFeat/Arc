package arc.graphics

import arc.culling.ArcFrustum
import arc.culling.Frustum
import arc.math.Point3d
import arc.math.Ray
import arc.math.Vec3f
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

internal data class ArcCamera(
    override var fov: Float,
    override var windowWidth: Float,
    override var windowHeight: Float,
) : Camera {

    override val ray: Ray = Ray.of(Vec3f.of(0f, 0f, 0f), Vec3f.of(0f, 0f, 0f))

    override var view: Matrix4f = Matrix4f()
    override var projection: Matrix4f = Matrix4f()
    override val combined: Matrix4f = Matrix4f()

    override var position: Point3d = Point3d.of(0.0, 0.0, 0.0)
    override var rotation: Quaternionf = Quaternionf()
    override var zNear: Float = 0.1f
    override var zFar: Float = 100f

    private var aspect = windowWidth / windowHeight

    // Reusable temp vectors
    private val tmpOrigin = Vector3f()
    private val tmpForward = Vector3f()
    private val tmpUp = Vector3f()

    override val frustum: Frustum = ArcFrustum(this)

    override fun rotate(pitch: Float, yaw: Float, roll: Float) {
        rotation.rotateXYZ(
            Math.toRadians(pitch),
            Math.toRadians(yaw),
            Math.toRadians(roll)
        )
    }

    override fun update() {
        projection.identity()
            .perspective(Math.toRadians(fov), aspect, zNear, zFar)

        tmpOrigin.set(position.x.toFloat(), position.y.toFloat(), position.z.toFloat())

        tmpForward.set(0f, 0f, -1f).rotate(rotation).normalize()
        val target = Vector3f(tmpOrigin).add(tmpForward)
        tmpUp.set(0f, 1f, 0f).rotate(rotation)

        view.identity().lookAt(tmpOrigin, target, tmpUp)
        combined.set(projection).mul(view)

        ray.origin.apply {
            this.x = tmpOrigin.x
            this.y = tmpOrigin.y
            this.z = tmpOrigin.z
        }
        ray.direction.apply {
            this.x = tmpForward.x
            this.y = tmpForward.y
            this.z = tmpForward.z
        }
    }

    override fun updateAspect(width: Int, height: Int) {
        if(height == 0) return
        if (windowHeight == height.toFloat() && windowWidth == width.toFloat()) return

        windowHeight = height.toFloat()
        windowWidth = width.toFloat()
        aspect = windowWidth / windowHeight
    }

    override fun reset() {
        view.identity()
        projection.identity()
        combined.set(projection).mul(view)
        rotation.identity()
        position = Point3d.of(0.0, 0.0, 0.0)
    }

    object Factory : Camera.Factory {
        override fun create(fov: Float, width: Float, height: Float): Camera {
            return ArcCamera(fov, width, height)
        }
    }
}