package arc.graphics.vertex

internal data class SimpleVertexFormat(
    override val elements: MutableList<VertexFormatElement> = mutableListOf(),
    override val offsets: MutableList<Int> = mutableListOf(),
) : VertexFormat {

    override var nextOffset = 0
    private var colorElementOffset = -1
    private val uvOffsetsById: MutableList<Int> = mutableListOf()
    override var normalElementOffset = -1

    override fun add(vertexFormatElement: VertexFormatElement) {
        if (vertexFormatElement.usage == VertexUsage.POSITION && this.hasPosition()) {
            return
        }

        elements.add(vertexFormatElement)
        offsets.add(this.nextOffset)

        when (vertexFormatElement.usage) {
            VertexUsage.NORMAL -> this.normalElementOffset = this.nextOffset
            VertexUsage.COLOR -> this.colorElementOffset = this.nextOffset
            VertexUsage.UV -> {
                while (uvOffsetsById.size <= vertexFormatElement.index) {
                    uvOffsetsById.add(-1)
                }
                uvOffsetsById[vertexFormatElement.index] = this.nextOffset
            }

            else -> {}
        }

        this.nextOffset += vertexFormatElement.size
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

    override fun getElementOffset(usage: VertexUsage): Int {
        for (element in elements) {
            if (element.usage == usage) {
                return element.count
            }
        }
        throw IllegalArgumentException("VertexUsage $usage not found in format")
    }

    override fun clear() {
        elements.clear()
        offsets.clear()
        this.colorElementOffset = -1
        uvOffsetsById.clear()
        this.normalElementOffset = -1
        this.nextOffset = 0
    }

    private fun hasPosition(): Boolean {
        return elements.any { it.usage == VertexUsage.POSITION }
    }

    class Builder : VertexFormat.Builder {

        private val elements = mutableListOf<VertexFormatElement>()

        override fun add(vertexFormat: VertexFormatElement): VertexFormat.Builder {
            elements.add(vertexFormat)
            return this
        }

        override fun build(): VertexFormat {
            return SimpleVertexFormat().also { vertex ->
                elements.forEach { vertex.add(it) }
            }
        }
    }
}