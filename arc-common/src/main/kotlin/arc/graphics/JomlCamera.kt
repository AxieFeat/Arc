package arc.graphics

import arc.culling.JomlFrustum
import arc.culling.Frustum
import arc.math.Point3d
import arc.math.Ray
import arc.math.Vec3f
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

internal data class JomlCamera(
    override var fov: Float,
    override var windowWidth: Float,
    override var windowHeight: Float,
) : Camera {

    override var ray: Ray = Ray.of(Vec3f.of(0f, 0f, 0f), Vec3f.of(0f, 0f, 0f))

    override var view: Matrix4f = Matrix4f()
    override var projection: Matrix4f = Matrix4f()
    override val combined: Matrix4f = Matrix4f()

    override var position: Point3d = Point3d.of(0.0, 0.0, 0.0)
    override var rotation: Quaternionf = Quaternionf()
    override var zNear: Float = 0.1f
    override var zFar: Float = 1000f

    private var aspect = windowWidth / windowHeight
    private var pitch = 0f
    private var yaw = 0f
    private var roll = 0f

    private val front = Vector3f(0f, 0f, -1f)
    private val up = Vector3f(0f, 1f, 0f)
    private val right = Vector3f(1f, 0f, 0f)

    override val frustum: Frustum = JomlFrustum()

    override fun rotate(pitch: Float, yaw: Float, roll: Float) {
        this.pitch = (this.pitch + pitch).coerceIn(-89f, 89f)
        this.yaw += yaw

        rotation.identity()
            .rotateY(Math.toRadians(-this.yaw))
            .rotateX(Math.toRadians(this.pitch))

        front.set(0f, 0f, -1f).rotate(rotation)
        right.set(1f, 0f, 0f).rotate(rotation)
        up.set(0f, 1f, 0f).rotate(rotation)
    }

    override fun update() {
        projection.identity()
            .perspective(Math.toRadians(fov), aspect, zNear, zFar)

        val cameraPos = Vector3f(position.x.toFloat(), position.y.toFloat(), position.z.toFloat())
        val cameraTarget = Vector3f(cameraPos).add(front)

        view.identity().lookAt(cameraPos, cameraTarget, up)
        combined.set(projection).mul(view)

        this.ray = ray.withOriginAndDirection(
            origin = Vec3f.of(
                cameraPos.x,
                cameraPos.y,
                cameraPos.z
            ),
            direction = Vec3f.of(
                front.x,
                front.y,
                front.z
            )
        )

        frustum.update(this)
    }

    override fun updateAspect(width: Int, height: Int) {
        if(height == 0) return
        if (windowHeight == height.toFloat() && windowWidth == width.toFloat()) return

        windowHeight = height.toFloat()
        windowWidth = width.toFloat()
        aspect = windowWidth / windowHeight

        update()
    }

    override fun reset() {
        view.identity()
        projection.identity()
        combined.set(projection).mul(view)
        rotation.identity()
        position = Point3d.of(0.0, 0.0, 0.0)
        pitch = 0f
        yaw = 0f
        roll = 0f
        front.set(0f, 0f, -1f)
        up.set(0f, 1f, 0f)
        right.set(1f, 0f, 0f)
        frustum.update(this)
    }

    object Factory : Camera.Factory {
        override fun create(fov: Float, width: Float, height: Float): Camera {
            return JomlCamera(fov, width, height)
        }
    }
}