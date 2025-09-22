package arc.graphics.vertex

/**
 * Represents types of vertex data used in rendering pipelines.
 *
 * @property size The size in bytes occupied by this vertex type.
 * @property typeName A descriptive name of the vertex type.
 */
@Suppress("UndocumentedPublicProperty") // TODO And documentation.
enum class VertexType(
    val size: Int,
    val typeName: String
) {

    FLOAT(4, "Float"),
    UBYTE(1, "Unsigned Byte"),
    BYTE(1, "Byte"),
    USHORT(2, "Unsigned Short"),
    SHORT(2, "Short"),
    UINT(4, "Unsigned Int"),
    INT(4, "Int");
}
