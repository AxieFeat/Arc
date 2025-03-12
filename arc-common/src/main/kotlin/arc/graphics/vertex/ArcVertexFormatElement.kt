package arc.graphics.vertex

internal data class ArcVertexFormatElement(
    override val index: Int,
    override val type: VertexType,
    override val usage: VertexUsage,
    override val count: Int,
) : VertexFormatElement {

    object Factory : VertexFormatElement.Factory {
        override fun create(
            index: Int,
            type: VertexType,
            usage: VertexUsage,
            count: Int,
        ): VertexFormatElement {
            return ArcVertexFormatElement(
                index, type, usage, count
            )
        }
    }
}