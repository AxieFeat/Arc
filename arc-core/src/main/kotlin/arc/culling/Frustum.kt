package arc.culling

import arc.graphics.Camera
import arc.math.AABB
import org.joml.Matrix4f

/**
 * This interface represents frustum culling in render.
 */
interface Frustum {

    /**
     * Is AABB in frustum.
     */
    fun isBoxInFrustum(aabb: AABB): Boolean

    /**
     * Is bounding box in frustum.
     */
    fun isBoxInFrustum(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float): Boolean

    /**
     * Update frustum with [Camera.combined] matrices.
     *
     * @param camera Camera for updating frustum.
     */
    fun update(camera: Camera) = update(camera.combined)

    /**
     * Update frustum with matrix.
     *
     * @param matrix Matrix for updating frustum.
     */
    fun update(matrix: Matrix4f)
}
