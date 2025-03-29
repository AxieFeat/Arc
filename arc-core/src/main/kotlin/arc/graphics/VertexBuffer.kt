package arc.graphics

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.graphics.vertex.VertexFormat
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents buffer of vertices.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface VertexBuffer {

    /**
     * ID of this buffer in Render system.
     */
    @get:JvmName("id")
    val id: Int

    /**
     * Vertex format of this buffer.
     */
    @get:JvmName("format")
    val format: VertexFormat

    /**
     * Mode of drawing of this buffer.
     */
    @get:JvmName("mode")
    val mode: DrawerMode

    /**
     * Count of vertices in this buffer.
     */
    @get:JvmName("size")
    val size: Int

    /**
     * Bind this buffer.
     */
    fun bind()

    /**
     * Unbind this buffer.
     */
    fun unbind()

    /**
     * Write new data in buffer.
     *
     * @param drawBuffer Data for writing.
     */
    fun bufferData(drawBuffer: DrawBuffer)

    /**
     * Clean resources of this buffer.
     */
    fun cleanup()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(buffer: DrawBuffer): VertexBuffer

    }

    companion object {

        /**
         * Create new instance of [VertexBuffer].
         *
         * @param buffer Buffer with vertices info.
         *
         * @return New instance of [VertexBuffer].
         */
        @JvmStatic
        fun create(buffer: DrawBuffer): VertexBuffer {
            return Arc.factory<Factory>().create(buffer)
        }

    }

}