package arc.model

import korlibs.math.geom.Vector3F

/**
 * Represents a directional face in a 3D coordinate system.
 *
 * The `Face` enum defines six possible orientations commonly used
 * in 3D spatial representations. These directions are relative
 * to a standard Cartesian coordinate system:
 * - `NORTH`: The positive Z-axis direction.
 * - `EAST`: The positive X-axis direction.
 * - `SOUTH`: The negative Z-axis direction.
 * - `WEST`: The negative X-axis direction.
 * - `UP`: The positive Y-axis direction.
 * - `DOWN`: The negative Y-axis direction.
 */
enum class Face(
    val normal: Vector3F
) {

    NORTH(Vector3F(0f, 0f, -1f)),
    EAST(Vector3F(1f, 0f, 0f)),
    SOUTH(Vector3F(0f, 0f, 1f)),
    WEST(Vector3F(-1f, 0f, 0f)),
    UP(Vector3F(0f, 1f, 0f)),
    DOWN(Vector3F(0f, -1f, 0f))
}
