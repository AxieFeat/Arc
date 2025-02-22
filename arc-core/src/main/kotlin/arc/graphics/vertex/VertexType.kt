package arc.graphics.vertex

/**
 * Represents types of vertex data used in rendering pipelines.
 *
 * This enum class defines the various data types that can be used for vertex attributes,
 * including their size (in bytes), descriptive type name, and corresponding type identifier.
 * These types are essential in specifying the format of vertex data passed to the graphics hardware.
 *
 * @property size The size in bytes occupied by this vertex type.
 * @property typeName A descriptive name of the vertex type.
 * @property id The unique identifier for this vertex type, typically aligned with graphics API enums.
 */
@Suppress("MemberVisibilityCanBePrivate")
enum class VertexType(
    val size: Int,
    val typeName: String,
    val id: Int
) {

    FLOAT(4, "Float", 5126),
    UBYTE(1, "Unsigned Byte", 5121),
    BYTE(1, "Byte", 5120),
    USHORT(2, "Unsigned Short", 5123),
    SHORT(2, "Short", 5122),
    UINT(4, "Unsigned Int", 5125),
    INT(4, "Int", 5124);

}