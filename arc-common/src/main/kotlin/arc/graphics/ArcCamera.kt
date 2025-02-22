package arc.graphics

import arc.math.Ray
import arc.math.Vec3f
import org.joml.Matrix4f

class ArcCamera : Camera {

    override var position: Vec3f = Vec3f.ZERO
    override var rotation: Vec3f = Vec3f.ZERO
    override val direction: Vec3f = Vec3f.ZERO
    override val up: Vec3f = Vec3f.ZERO

    override val projection: Matrix4f = Matrix4f()
    override val view: Matrix4f = Matrix4f()
    override val combined: Matrix4f = Matrix4f()
    override var inv: Matrix4f = Matrix4f()

    override var fov: Float = 75f
    override var near: Float = 1f
    override var far: Float = 100f
    override var perspective: Boolean = true
    override var width: Float = 0f
    override var height: Float = 0f
    override val ray: Ray = Ray.ZERO

    override fun update() {

    }

    override fun resize(viewportWidth: Float, viewportHeight: Float) {

    }

    override fun lookAt(vec3f: Vec3f) {

    }
}