package arc.culling

import arc.graphics.Camera
import arc.math.AABB

/**
 * This interface represents frustum culling in render.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface Frustum {

    /**
     * Camera of this frustum.
     */
    @get:JvmName("position")
    val camera: Camera

    /**
     * Is AABB in frustum.
     */
    fun isBoxInFrustum(aabb: AABB): Boolean

    /**
     * Is bounding box in frustum.
     */
    fun isBoxInFrustum(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float): Boolean

}