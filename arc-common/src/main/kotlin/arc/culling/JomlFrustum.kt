package arc.culling

import arc.graphics.Camera
import arc.math.AABB
import org.joml.FrustumIntersection

internal class JomlFrustum : Frustum {

    private val frustumIntersection = FrustumIntersection()

    override fun isBoxInFrustum(aabb: AABB): Boolean {
        return isBoxInFrustum(
            aabb.min.x, aabb.min.y, aabb.min.z,
            aabb.max.x, aabb.max.y, aabb.max.z
        )
    }

    override fun isBoxInFrustum(
        minX: Float, minY: Float, minZ: Float,
        maxX: Float, maxY: Float, maxZ: Float
    ): Boolean {
        return frustumIntersection.testAab(minX, minY, minZ, maxX, maxY, maxZ)
    }

    override fun update(camera: Camera) {
        frustumIntersection.set(camera.combined)
    }
}
