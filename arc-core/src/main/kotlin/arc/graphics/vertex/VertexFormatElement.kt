package arc.graphics.vertex

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents an immutable element in a vertex format.
 */
@ImmutableType
@Suppress("INAPPLICABLE_JVM_NAME")
interface VertexFormatElement {

    /**
     * Represents the index of a vertex format element within a [VertexFormat].
     *
     * The value of `index` is primarily used to identify the position of the element
     * within a sequence of vertex attributes, allowing for precise data interpretation
     * and processing during rendering or storage operations.
     */
    @get:JvmName("index")
    val index: Int

    /**
     * Represents the data type of the vertex attribute in the [VertexFormatElement].
     *
     * This property defines the type of data used for a vertex format element,
     * corresponding to a specific [VertexType] enumeration value. The type determines
     * the structure, size, and rendering behavior of the vertex attribute.
     *
     * @see VertexType
     * @see VertexFormatElement
     */
    @get:JvmName("type")
    val type: VertexType

    /**
     * Represents the usage type of a vertex attribute in the [VertexFormatElement].
     *
     * This property provides access to the specific [VertexUsage] associated with the vertex format
     * element, defining how the data within the element is intended to be used (e.g., position, color,
     * normal, UV, etc.).
     *
     * @see VertexUsage
     */
    @get:JvmName("usage")
    val usage: VertexUsage

    /**
     * Represents the count of elements in this vertex format element.
     *
     * The `count` property indicates the number of individual elements that this
     * vertex format element contains. This is often used in the context of describing
     * the quantity of components (e.g., coordinates, colors, normals) tied to a
     * specific vertex attribute within a rendering pipeline.
     */
    @get:JvmName("count")
    val count: Int

    /**
     * Get size of this element.
     */
    @get:JvmName("size")
    val size: Int
        get() = type.size * this.count

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(
            index: Int,
            type: VertexType,
            usage: VertexUsage,
            count: Int,
        ): VertexFormatElement

    }

    companion object {

        @JvmField val POSITION = create(0, VertexType.FLOAT, VertexUsage.POSITION, 3)
        @JvmField val COLOR = create(0, VertexType.UBYTE, VertexUsage.COLOR, 4)
        @JvmField val UV0 = create(0, VertexType.FLOAT, VertexUsage.UV, 2)
        @JvmField val UV1 = create(1, VertexType.SHORT, VertexUsage.UV, 2)
        @JvmField val UV2 = create(2, VertexType.SHORT, VertexUsage.UV, 2)
        @JvmField val NORMAL = create(0, VertexType.BYTE, VertexUsage.NORMAL, 3)
        @JvmField val PADDING = create(0, VertexType.BYTE, VertexUsage.PADDING, 1)

        /**
         * Creates a new instance of [VertexFormatElement] with the specified properties.
         *
         * @param index The position of the vertex element in the format.
         * @param type The data type of the vertex element, defined by [VertexType].
         * @param usage The usage type of the vertex element, defined by [VertexUsage].
         * @param count The number of components in this vertex element.
         *
         * @return A newly created [VertexFormatElement] with the defined properties.
         */
        @JvmStatic
        fun create(
            index: Int,
            type: VertexType,
            usage: VertexUsage,
            count: Int,
        ): VertexFormatElement {
            return Arc.factory<Factory>().create(index, type, usage, count)
        }

    }

}