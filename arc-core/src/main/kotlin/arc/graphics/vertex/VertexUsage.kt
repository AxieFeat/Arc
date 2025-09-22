package arc.graphics.vertex

/**
 * Represents the types of usage for vertex attributes in a vertex format.
 *
 * This enum defines common categories for how vertex data is used in a rendering pipeline,
 * such as position, normals, colors, and texture coordinates. Each value in this enum has an
 * associated string name that describes the usage type.
 *
 * @property usageName Descriptive name for the type of vertex usage.
 */
@Suppress("UndocumentedPublicProperty") // TODO And documentation.
enum class VertexUsage(
    val usageName: String
) {

    POSITION("Position"),
    NORMAL("Normal"),
    COLOR("Vertex Color"),
    UV("UV"),
    PADDING("Padding");
}
