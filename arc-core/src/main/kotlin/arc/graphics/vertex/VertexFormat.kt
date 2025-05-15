package arc.graphics.vertex

import arc.Arc
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a format structure for defining vertex attributes and their properties.
 * With VertexFormat you designate what parameters the shader accepts.
 *
 * VertexFormat allows you to control the DrawBuffer and what parameters will be provided to the shader.
 *
 * In example, we have these VertexFormat:
 * ```kotlin
 * val format = VertexFormat.builder()
 *         .add(VertexFormatElement.POSITION) // <- First element should be position ALWAYS!
 *         .add(VertexFormatElement.COLOR) // <- Any elements, example color.
 *         .build()
 * ```
 * With this format we can create buffer and also it says, what data should be provided to shader.
 * How it works with [arc.graphics.DrawBuffer] you can see in it class.
 *
 * For providing vertex data to shaders you should use layouts. Example for VertexFormat above:
 * ```glsl
 * layout (location = 0) in vec3 Position; // <- location = 0 - always is Position.
 * layout (location = 1) in vec4 Color; // <- location = 1 - Our color argument.
 * ```
 * In these example we use position (vec3) and color (vec4) - what about other types?
 * ```plain
 * (That's not all types)
 *
 * VertexFormatElement.POSITION -> vec3
 * VertexFormatElement.COLOR -> vec4
 * VertexFormatElement.UV -> vec2
 * ```
 * To find out what data type a [VertexFormatElement] will have, look at its [VertexType] and [VertexFormatElement.count].
 * In example [VertexFormatElement.POSITION] have [VertexType.FLOAT] -
 * this means that the data type in the shader will be `float`.
 * And it [VertexFormatElement.count] equals 3 - this means that the data type in the shader will have 3 such values
 * (it turns out 3 `float` values in this case).
 * In result, we have `vec3` in glsl shader - vector with 3 `float` values.
 */
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
    @ApiStatus.Internal
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

    companion object {

        /**
         * Create new instance of [Builder].
         */
        @JvmStatic
        fun builder(): Builder {
            return Arc.factory<Builder>()
        }

    }

}