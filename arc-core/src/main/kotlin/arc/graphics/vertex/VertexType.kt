package arc.graphics.vertex

/**
 * Represents types of vertex data used in rendering pipelines.
 *
 * @property size The size in bytes occupied by this vertex type.
 * @property typeName A descriptive name of the vertex type.
 * @property id Id in render system.
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