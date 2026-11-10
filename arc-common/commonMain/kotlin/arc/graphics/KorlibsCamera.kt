package arc.graphics

import arc.culling.Frustum
import arc.culling.KorlibsFrustum
import arc.math.Ray
import korlibs.math.geom.Matrix4
import korlibs.math.geom.Quaternion
import korlibs.math.geom.Vector3F
import korlibs.math.geom.degrees

internal class KorlibsCamera(
    override var fov: Float,
    override var windowWidth: Float,
    override var windowHeight: Float,
) : Camera {
    override var ray: Ray = Ray.of(Vector3F(0f, 0f, 0f), Vector3F(0f, 0f, 0f))
    override var view: Matrix4 = Matrix4()
    override var projection: Matrix4 = Matrix4()
    override var combined: Matrix4 = Matrix4()
        private set
    override var position: Vector3F = Vector3F(0f, 0f, 0f)
    override var rotation: Quaternion = Quaternion()
    override var zNear: Float = 0.1f
    override var zFar: Float = 1000f

    private var aspect = windowWidth / windowHeight
    private var pitch = 0f
    private var yaw = 0f
    private var front = Vector3F(0f, 0f, -1f)
    private var up = Vector3F(0f, 1f, 0f)
    private var right = Vector3F(1f, 0f, 0f)

    override val frustum: Frustum = KorlibsFrustum()

    override fun rotate(pitch: Float, yaw: Float, roll: Float) {
        this.pitch = (this.pitch + pitch).coerceIn(MIN_CAMERA_PITCH, MAX_CAMERA_PITCH)
        this.yaw += yaw
        rotation = Quaternion.fromAxisAngle(Vector3F(0f, 1f, 0f), (-this.yaw).degrees) *
            Quaternion.fromAxisAngle(Vector3F(1f, 0f, 0f), this.pitch.degrees)
        front = rotation.transform(Vector3F(0f, 0f, -1f))
        right = rotation.transform(Vector3F(1f, 0f, 0f))
        up = rotation.transform(Vector3F(0f, 1f, 0f))
    }

    override fun update() {
        projection = Matrix4.perspective(fov.degrees, aspect, zNear, zFar)
        // Korlibs 6.1 lookAt constructs orientation only, without the eye translation.
        view = Matrix4.fromRows(
            right.x, right.y, right.z, -(right.x * position.x + right.y * position.y + right.z * position.z),
            up.x, up.y, up.z, -(up.x * position.x + up.y * position.y + up.z * position.z),
            -front.x, -front.y, -front.z, front.x * position.x + front.y * position.y + front.z * position.z,
            0f, 0f, 0f, 1f
        )
        combined = projection * view
        ray = ray.withOriginAndDirection(position, front)
        frustum.update(combined)
    }

    override fun updateAspect(width: Int, height: Int) {
        if (height == 0) return
        if (windowHeight == height.toFloat() && windowWidth == width.toFloat()) return
        windowHeight = height.toFloat()
        windowWidth = width.toFloat()
        aspect = windowWidth / windowHeight
        update()
    }

    override fun reset() {
        view = Matrix4()
        projection = Matrix4()
        combined = Matrix4()
        rotation = Quaternion()
        position = Vector3F(0f, 0f, 0f)
        pitch = 0f
        yaw = 0f
        front = Vector3F(0f, 0f, -1f)
        up = Vector3F(0f, 1f, 0f)
        right = Vector3F(1f, 0f, 0f)
        frustum.update(combined)
    }

    object Factory : Camera.Factory {
        override fun create(fov: Float, width: Float, height: Float): Camera = KorlibsCamera(fov, width, height)
    }

    companion object {
        const val MIN_CAMERA_PITCH = -89f
        const val MAX_CAMERA_PITCH = 89f
    }
}
