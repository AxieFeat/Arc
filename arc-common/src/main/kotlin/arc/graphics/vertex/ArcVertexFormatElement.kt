package arc.graphics.vertex

data class ArcVertexFormatElement(
    override val name: String,
    override val id: Int,
    override val index: Int,
    override val type: VertexType,
    override val usage: VertexUsage,
    override val count: Int,
    override val attributeIndex: Int
) : VertexFormatElement {

    override val byteSize: Int
        get() = type.size * this.count

    override val mask: Int
        get() = 1 shl this.id

    object Factory : VertexFormatElement.Factory {
        override fun create(
            name: String,
            id: Int,
            index: Int,
            type: VertexType,
            usage: VertexUsage,
            count: Int,
            attributeIndex: Int
        ): VertexFormatElement {
            return ArcVertexFormatElement(
                name, id, index, type, usage, count, attributeIndex
            )
        }

    }

}