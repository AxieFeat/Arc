package arc.graphics.vertex

internal data class SimpleVertexFormatElement(
    override val name: String,
    override val index: Int,
    override val type: VertexType,
    override val usage: VertexUsage,
    override val count: Int,
) : VertexFormatElement {

    object Factory : VertexFormatElement.Factory {

        override fun create(
            name: String,
            index: Int,
            type: VertexType,
            usage: VertexUsage,
            count: Int,
        ): VertexFormatElement {
            return SimpleVertexFormatElement(
                name, index, type, usage, count
            )
        }
    }
}
