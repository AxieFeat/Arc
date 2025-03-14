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
@Suppress("INAPPLICABLE_JVM_NAME")
interface VertexFormat {

    /**
     * Elements of this format.
     */
    @get:JvmName("elements")
    val elements: List<VertexFormatElement>

    /**
     * Offsets list.
     */
    @get:JvmName("offsets")
    val offsets: List<Int>

    /**
     * Offset of next element.
     */
    @get:JvmName("nextOffset")
    val nextOffset: Int

    /**
     * Normalized element offset.
     */
    @get:JvmName("normalElementOffset")
    val normalElementOffset: Int

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

    fun getElementOffset(usage: VertexUsage): Int

    fun getOffset(index: Int): Int

    fun clear()

    /**
     * Use this interface to build own [VertexFormat].
     */
    @MutableType
    interface Builder : arc.util.Builder<VertexFormat> {

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

        /**
         * Create new instance of [Builder].
         */
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