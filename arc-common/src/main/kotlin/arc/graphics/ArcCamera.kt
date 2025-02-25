package arc.graphics

import arc.math.Point3d
import arc.math.Ray
import arc.math.Vec3f

internal class ArcCamera : Camera {

    override var position: Point3d = Point3d.ZERO
    override var rotation: Vec3f = Vec3f.ZERO

    override var fov: Float = 75f
    override val ray: Ray = Ray.ZERO

    override fun lookAt(vec3f: Vec3f) {
        // TODO
    }
}