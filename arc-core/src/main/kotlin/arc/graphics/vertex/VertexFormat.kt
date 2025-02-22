package arc.graphics.vertex

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a format structure for defining vertex attributes and their properties.
 *
 * This interface allows for defining and managing a vertex format with attributes such as
 * offsets, elements, and their mappings. It provides utility methods to check for the existence
 * of specific elements and to retrieve elements or their names by reference.
 */
@ImmutableType
@Suppress("INAPPLICABLE_JVM_NAME")
interface VertexFormat {

//    /**
//     * Size of [VertexFormat].
//     */
//    @get:JvmName("vertexSize")
//    val vertexSize: Int

    /**
     * The `offset` property represents the byte offset of this vertex format within a data structure.
     * It is used to determine the starting position of the vertex data for processing, storage, or rendering.
     */
    @get:JvmName("offset")
    val offset: Int

//    /**
//     * Element mask.
//     */
//    val elementMask: Int

    /**
     * A map of vertex format elements used by the [VertexFormat]. Each entry associates a string key
     * (representing the name of the element) with a specific [VertexFormatElement] instance.
     *
     * This property provides access to all elements defined in the vertex format, allowing retrieval
     * or inspection of their properties via their mapped names.
     */
    @get:JvmName("elements")
    val elements: Map<String, VertexFormatElement>

    /**
     * Is [VertexFormat] contains some [VertexFormatElement].
     *
     * @param vertexFormatElement Some vertex format element.
     *
     * @return Is [VertexFormat] contains vertex element.
     */
    fun contains(vertexFormatElement: VertexFormatElement): Boolean

    /**
     * Get name of some [VertexFormatElement] in this [VertexFormat].
     *
     * @param vertexFormatElement Some vertex format element.
     *
     * @return Name of element or null if not present.
     */
    fun nameOf(vertexFormatElement: VertexFormatElement): String?

    /**
     * Get element from [elements] by [name].
     *
     * @param name Name of element.
     *
     * @return Instance of [VertexFormatElement] or null if not found.
     */
    fun getElement(name: String): VertexFormatElement?

    /**
     * Use this interface to build own [VertexFormat].
     */
    interface Builder : arc.util.Builder<VertexFormat> {

        /**
         * Add vertex parameter.
         *
         * @param name Name of parameter.
         * @param vertexFormat Vertex format.
         *
         * @return Current instance of [Builder].
         */
        fun add(name: String, vertexFormat: VertexFormatElement): Builder

        /**
         * Add offset for [VertexFormat].
         *
         * @param offset Offset size.
         *
         * @return Current instance of [Builder].
         */
        fun padding(offset: Int): Builder

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