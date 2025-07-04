package arc.graphics

import arc.culling.JomlFrustum
import arc.culling.Frustum
import arc.math.Ray
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

internal data class JomlCamera(
    override var fov: Float,
    override var windowWidth: Float,
    override var windowHeight: Float,
) : Camera {

    override var ray: Ray = Ray.of(Vector3f(), Vector3f())

    override var view: Matrix4f = Matrix4f()
    override var projection: Matrix4f = Matrix4f()
    override val combined: Matrix4f = Matrix4f()

    override var position: Vector3f = Vector3f(0f, 0f, 0f)
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

        val cameraTarget = Vector3f(position).add(front)

        view.identity().lookAt(position, cameraTarget, up)
        combined.set(projection).mul(view)

        this.ray = ray.withOriginAndDirection(
            origin = Vector3f(
                position.x,
                position.y,
                position.z
            ),
            direction = Vector3f(
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
        position = Vector3f(0f, 0f, 0f)
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