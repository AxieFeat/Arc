package arc.graphics.vertex

internal  data class ArcVertexFormat(
    override val elements: MutableMap<String, VertexFormatElement>,
    override val offset: Int,
    //override val vertexSize: Int
) : VertexFormat {

//    override val elementMask: Int = run {
//        elements.stream().mapToInt(VertexFormatElement::mask).reduce(0) { ix: Int, jx: Int -> ix or jx }
//    }

    override fun contains(vertexFormatElement: VertexFormatElement): Boolean {
        return elements.containsValue(vertexFormatElement)
    }

    override fun nameOf(vertexFormatElement: VertexFormatElement): String? {
        if(!elements.containsValue(vertexFormatElement)) return null

        return TODO()
    }

    override fun getElement(name: String): VertexFormatElement? {
        return elements[name]
    }

    class Builder : VertexFormat.Builder {

        private val elements = mutableMapOf<String, VertexFormatElement>()
        private var offset = 0

        override fun add(name: String, vertexFormat: VertexFormatElement): VertexFormat.Builder {
            elements[name] = vertexFormat
            // TODO
//            offset += vertexFormat.byteSize

            return this
        }

        override fun padding(offset: Int): VertexFormat.Builder {
            this.offset += offset

            return this
        }

        override fun build(): VertexFormat {
            return ArcVertexFormat(elements, offset)
        }

    }

    object BuilderFactory : VertexFormat.BuilderFactory {
        override fun create(): VertexFormat.Builder {
            return Builder()
        }

    }


}