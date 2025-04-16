package arc.graphics

/**
 * Enum class representing different drawing modes for primitives.
 */
enum class DrawerMode(
    val id: Int,
) {

    LINES(1),
    LINE_STRIP(3),
    TRIANGLES(4),
    TRIANGLE_STRIP(5),
    TRIANGLE_FAN(6),

    @Deprecated("In OpenGL 4.1 QUADS are deprecated drawing mode")
    QUADS(7)

}