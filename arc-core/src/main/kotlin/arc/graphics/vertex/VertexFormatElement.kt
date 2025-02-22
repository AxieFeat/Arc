package arc.graphics.vertex

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents an immutable element in a vertex format.
 *
 * A vertex format element describes a single component of a vertex format, such as its name, ID, type, usage, and
 * various other properties relevant for rendering systems. It defines the layout of vertex data in buffers, used in
 * rendering pipelines.
 */
@ImmutableType
@Suppress("INAPPLICABLE_JVM_NAME")
interface VertexFormatElement {

    /**
     * The `name` property represents the name of the vertex attribute defined in the
     * `VertexFormatElement` class. This name uniquely identifies the attribute and is used
     * in defining and organizing vertex data within a rendering context.
     *
     * It plays a crucial role in mapping vertex attributes to the corresponding shader inputs
     * or data structures used for rendering.
     */
    @get:JvmName("name")
    val name: String

    /**
     * Represents the identifier for the vertex format element.
     */
    @get:JvmName("id")
    val id: Int

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
     * Represents the index of a vertex attribute in the vertex format.
     *
     * This property is typically used to reference a specific attribute, such as position, color, or UV,
     * within OpenGL or other rendering systems during shader operations. Each attribute is uniquely
     * identified by its index in the associated vertex format, and this index is critical for binding
     * and accessing attribute data efficiently.
     */
    @get:JvmName("attributeIndex")
    val attributeIndex: Int

    /**
     * Represents a bitmask defining specific properties or configurations for this vertex format element.
     * The `mask` is used to specify a set of flags or attributes related to the element's usage, enabling
     * efficient processing and rendering in a vertex format setup.
     */
    @get:JvmName("mask")
    val mask: Int

    /**
     * Represents the byte size of the vertex format element.
     *
     * This property returns the total number of bytes occupied by the specific
     * [VertexFormatElement] based on its [VertexType] and configuration. It defines
     * how much memory space is allocated for the element in buffer storage or during
     * rendering operations.
     *
     * The byte size is typically used when computing offsets or total size requirements
     * for vertex data in rendering pipelines.
     */
    @get:JvmName("byteSize")
    val byteSize: Int

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Creates a new instance of [VertexFormatElement] with the specified properties.
         *
         * @param name The name of the vertex attribute.
         * @param id The identifier for the vertex element.
         * @param index The position of the vertex element in the format.
         * @param type The data type of the vertex element, defined by [VertexType].
         * @param usage The usage type of the vertex element, defined by [VertexUsage].
         * @param count The number of components in this vertex element.
         * @param attributeIndex The index of the attribute in the rendering system.
         * @return A newly created [VertexFormatElement] with the defined properties.
         */
        fun create(
            name: String,
            id: Int,
            index: Int,
            type: VertexType,
            usage: VertexUsage,
            count: Int,
            attributeIndex: Int
        ): VertexFormatElement

    }

    companion object {

        @JvmField val POSITION = create("POSITION_3F", 0, 0, VertexType.FLOAT, VertexUsage.POSITION, 3, 0)
        @JvmField val COLOR = create("COLOR_4UB", 1, 0, VertexType.UBYTE, VertexUsage.COLOR, 4, 1)
        @JvmField val UV0 = create("TEX_2F", 2, 0, VertexType.FLOAT, VertexUsage.UV, 2, 2)
        @JvmField val UV1 = create("TEX_2S", 3, 1, VertexType.SHORT, VertexUsage.UV, 2, 3)
        @JvmField val UV2 = create("TEX_2SB", 4, 2, VertexType.SHORT, VertexUsage.UV, 2, 4)
        @JvmField val NORMAL = create("NORMAL_3B", 5, 0, VertexType.BYTE, VertexUsage.NORMAL, 3, 5)

        /**
         * Creates a new instance of [VertexFormatElement] with the specified properties.
         *
         * @param name The name of the vertex attribute.
         * @param id The identifier for the vertex element.
         * @param index The position of the vertex element in the format.
         * @param type The data type of the vertex element, defined by [VertexType].
         * @param usage The usage type of the vertex element, defined by [VertexUsage].
         * @param count The number of components in this vertex element.
         * @param attributeIndex The index of the attribute in the rendering system.
         * @return A newly created [VertexFormatElement] with the defined properties.
         */
        @JvmStatic
        fun create(
            name: String,
            id: Int,
            index: Int,
            type: VertexType,
            usage: VertexUsage,
            count: Int,
            attributeIndex: Int
        ): VertexFormatElement {
            return Arc.factory<Factory>().create(name, id, index, type, usage, count, attributeIndex)
        }

    }

}