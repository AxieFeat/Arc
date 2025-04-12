package arc.graphics.vertex

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents an immutable element in a vertex format.
 */
@ImmutableType
interface VertexFormatElement {

    /**
     * Name of element. Used just for debugging.
     */
    val name: String

    /**
     * Represents the index of a vertex format element within a [VertexFormat].
     */
    val index: Int

    /**
     * Represents the data type of the vertex attribute in the [VertexFormatElement].
     */
    val type: VertexType

    /**
     * Represents the usage type of a vertex attribute in the [VertexFormatElement].
     */
    val usage: VertexUsage

    /**
     * Represents the count of elements in this vertex format element.
     */
    val count: Int

    /**
     * Get size of this element.
     */
    val size: Int
        get() = type.size * this.count

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(
            name: String,
            index: Int,
            type: VertexType,
            usage: VertexUsage,
            count: Int,
        ): VertexFormatElement

    }

    companion object {

        @JvmField val POSITION = create("POSITION", 0, VertexType.FLOAT, VertexUsage.POSITION, 3)
        @JvmField val COLOR = create("COLOR", 0, VertexType.UBYTE, VertexUsage.COLOR, 4)
        @JvmField val UV = create("UV", 0, VertexType.FLOAT, VertexUsage.UV, 2)
        @JvmField val OVERLAY = create("OVERLAY", 1, VertexType.SHORT, VertexUsage.UV, 2)
        @JvmField val LIGHT = create("LIGHT", 2, VertexType.SHORT, VertexUsage.UV, 2)
        @JvmField val NORMAL = create("NORMAL", 0, VertexType.BYTE, VertexUsage.NORMAL, 3)
        @JvmField val PADDING = create("PADDING", 0, VertexType.BYTE, VertexUsage.PADDING, 1)

        /**
         * Creates a new instance of [VertexFormatElement] with the specified properties.
         *
         * @param name The name of the vertex element in the format.
         * @param index The position of the vertex element in the format.
         * @param type The data type of the vertex element, defined by [VertexType].
         * @param usage The usage type of the vertex element, defined by [VertexUsage].
         * @param count The number of components in this vertex element.
         *
         * @return A newly created [VertexFormatElement] with the defined properties.
         */
        @JvmStatic
        fun create(
            name: String,
            index: Int,
            type: VertexType,
            usage: VertexUsage,
            count: Int,
        ): VertexFormatElement {
            return Arc.factory<Factory>().create(name, index, type, usage, count)
        }

    }

}