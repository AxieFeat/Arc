package arc.model

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
 *
 * This enum is often used in the context of 3D modeling,
 * chunk-based environments, or cube-based structures to identify
 * specific faces of an object.
 */
enum class Face {

    NORTH,
    EAST,
    SOUTH,
    WEST,
    UP,
    DOWN

}