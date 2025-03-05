package arc.graphics

/**
 * Enum class representing different drawing modes for primitives.
 *
 * Each mode specifies how geometric primitives are constructed and rendered,
 * defining their connectivity, size, and behavior during rendering.
 *
 * @property id An identifier for the render system.
 * @property primitiveLength The number of vertices required to define a complete primitive.
 * @property primitiveStride The step size between primitives in terms of vertices.
 * @property connectedPrimitives Indicates if the primitives in this mode are connected consecutively.
 */
enum class DrawerMode(
    val id: Int,
    val primitiveLength: Int,
    val primitiveStride: Int,
    val connectedPrimitives: Boolean
) {

    LINES(1, 2, 2, false),
    LINE_STRIP(3, 2, 1, true),
    TRIANGLES(4, 3, 3, false),
    TRIANGLE_STRIP(5, 3, 1, true),
    TRIANGLE_FAN(6, 3, 1, true),
    QUADS(7, 4, 4, false);

}