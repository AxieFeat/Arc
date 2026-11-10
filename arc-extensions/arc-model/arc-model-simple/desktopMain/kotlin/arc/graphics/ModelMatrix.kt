package arc.graphics

import korlibs.math.geom.Matrix4

internal class ModelMatrix(var value: Matrix4 = Matrix4())

/**
 * Returns the inverse-transpose of the upper 3x3 part with identity translation.
 */
internal fun Matrix4.normalMatrix(): Matrix4 = Matrix4.fromRows(
    v00, v01, v02, 0f,
    v10, v11, v12, 0f,
    v20, v21, v22, 0f,
    0f, 0f, 0f, 1f
).inverted().transposed()
