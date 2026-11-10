package arc.culling

import arc.math.AABB
import korlibs.math.geom.Matrix4

internal class KorlibsFrustum : Frustum {
    // Six inward-facing clip planes, four coefficients each. Normalization is unnecessary for AABB tests.
    private val planes = FloatArray(24)
    private val matrixRows = FloatArray(16)

    override fun isBoxInFrustum(aabb: AABB): Boolean = isBoxInFrustum(
        aabb.min.x, aabb.min.y, aabb.min.z,
        aabb.max.x, aabb.max.y, aabb.max.z
    )

    override fun isBoxInFrustum(
        minX: Float, minY: Float, minZ: Float,
        maxX: Float, maxY: Float, maxZ: Float
    ): Boolean {
        for (i in planes.indices step 4) {
            val x = planes[i]
            val y = planes[i + 1]
            val z = planes[i + 2]
            if (x * (if (x >= 0f) maxX else minX) +
                y * (if (y >= 0f) maxY else minY) +
                z * (if (z >= 0f) maxZ else minZ) + planes[i + 3] < 0f
            ) return false
        }
        return true
    }

    override fun update(matrix: Matrix4) {
        matrix.copyToRows(matrixRows)
        for (axis in 0..2) {
            for (column in 0..3) {
                planes[axis * 8 + column] = matrixRows[12 + column] + matrixRows[axis * 4 + column]
                planes[axis * 8 + 4 + column] = matrixRows[12 + column] - matrixRows[axis * 4 + column]
            }
        }
    }
}
