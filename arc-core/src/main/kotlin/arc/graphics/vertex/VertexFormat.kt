package arc.graphics.vertex

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a format structure for defining vertex attributes and their properties.
 *
 * With VertexFormat you designate what parameters the shader accepts.
 */
@MutableType
interface VertexFormat {

    /**
     * Elements of this format.
     */
    val elements: List<VertexFormatElement>

    /**
     * Offsets list.
     */
    val offsets: List<Int>

    /**
     * Offset of next element.
     */
    val nextOffset: Int

    /**
     * Normalized element offset.
     */
    val normalElementOffset: Int

    /**
     * Count of elements in this format
     */
    val count: Int
        get() = elements.size

    /**
     * Add [vertexFormatElement] element for this format.
     *
     * @param vertexFormatElement Some vertex format element.
     */
    fun add(vertexFormatElement: VertexFormatElement)

    /**
     * Is [VertexFormat] contains some [VertexFormatElement].
     *
     * @param vertexFormatElement Some vertex format element.
     *
     * @return Is [VertexFormat] contains vertex element.
     */
    fun contains(vertexFormatElement: VertexFormatElement): Boolean

    /**
     * Get element from [elements] by [index].
     *
     * @param index Index of element.
     *
     * @return Instance of [VertexFormatElement] .
     */
    fun getElement(index: Int): VertexFormatElement

    /**
     * Get offset for some type.
     *
     * @param usage Type of usage.
     *
     * @return Value of offset for this [usage].
     */
    fun getElementOffset(usage: VertexUsage): Int

    /**
     * Get offset by index.
     *
     * @param index Index in list.
     *
     * @return Value from list by index.
     */
    fun getOffset(index: Int): Int

    /**
     * Clear current instance.
     */
    fun clear()

    /**
     * Use this interface to build own [VertexFormat].
     */
    interface Builder : arc.util.pattern.Builder<VertexFormat> {

        /**
         * Add vertex parameter.
         *
         * @param vertexFormat Vertex format.
         *
         * @return Current instance of [Builder].
         */
        fun add(vertexFormat: VertexFormatElement): Builder

        /**
         * Build [VertexFormat].
         *
         * @return New instance of [VertexFormat] with settings from buffer.
         */
        override fun build(): VertexFormat

    }

    @ApiStatus.Internal
    @TypeFactory
    interface BuilderFactory {

        fun create(): Builder

    }

    companion object {

        /**
         * Create new instance of [Builder].
         */
        @JvmStatic
        fun builder(): Builder {
            return Arc.factory<BuilderFactory>().create()
        }

    }

}