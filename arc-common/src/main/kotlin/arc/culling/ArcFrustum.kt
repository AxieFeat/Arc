package arc.culling

import arc.graphics.Camera
import arc.math.AABB

// TODO
internal class ArcFrustum(
    override var camera: Camera
) : Frustum {

    override fun isBoxInFrustum(aabb: AABB): Boolean {
        return false
    }

    override fun isBoxInFrustum(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float): Boolean {
        return false
    }

}