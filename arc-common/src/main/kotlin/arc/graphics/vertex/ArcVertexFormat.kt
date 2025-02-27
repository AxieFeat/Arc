package arc.graphics.vertex

internal data class ArcVertexFormat(
    override val elements: MutableList<VertexFormatElement> = mutableListOf(),
    override val offsets: MutableList<Int> = mutableListOf()
) : VertexFormat {

    override fun add(vertexFormatElement: VertexFormatElement) {
        elements.add(vertexFormatElement)
    }

    override fun contains(vertexFormatElement: VertexFormatElement): Boolean {
        return elements.contains(vertexFormatElement)
    }

    override fun getElement(index: Int): VertexFormatElement {
        return elements[index]
    }

    override fun getOffset(index: Int): Int {
        return offsets[index]
    }

    class Builder : VertexFormat.Builder {

        private val elements = mutableListOf<VertexFormatElement>()
        private val offsets = mutableListOf<Int>()

        override fun add(vertexFormat: VertexFormatElement): VertexFormat.Builder {
            elements.add(vertexFormat)
            // TODO
//            offset += vertexFormat.byteSize

            return this
        }

        override fun padding(offset: Int): VertexFormat.Builder {
//            this.offset += offset

            return this
        }

        override fun build(): VertexFormat {
            return ArcVertexFormat(elements, offsets)
        }

    }

    object BuilderFactory : VertexFormat.BuilderFactory {
        override fun create(): VertexFormat.Builder {
            return Builder()
        }

    }


}