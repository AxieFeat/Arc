package arc.graphics.vertex

import arc.Arc.single
import arc.annotations.TypeFactory
import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.util.pattern.Bindable
import arc.util.pattern.Cleanable
import org.jetbrains.annotations.ApiStatus
import java.nio.ByteBuffer

/**
 * This interface represents a buffer of vertices.
 *
 * @sample arc.sample.meshSample
 */
interface VertexBuffer : Bindable, Cleanable {

    /**
     * ID of this buffer in a Render system.
     */
    val id: Int

    /**
     * Vertex format of this buffer.
     */
    val format: VertexFormat

    /**
     * Mode of drawing of this buffer.
     */
    val mode: DrawerMode

    /**
     * Size of this buffer.
     */
    val size: Int

    /**
     * Count of vertices in the buffer.
     */
    val vertices: Int

    /**
     * Store some [ByteBuffer] in this vertex buffer (It will overwrite old values).
     *
     * @param buffer Buffer for writing it this vertex buffer instance.
     * @param vertices Count of vertices in the buffer.
     */
    fun write(buffer: ByteBuffer, vertices: Int)

    /**
     * Store some [DrawBuffer] in this vertex buffer (It will overwrite old values).
     *
     * @param buffer Draw buffer with vertex data.
     */
    fun write(buffer: DrawBuffer) = write(buffer.byteBuffer, buffer.vertexCount)

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(mode: DrawerMode, format: VertexFormat, buffer: ByteBuffer, vertices: Int): VertexBuffer

    }

    companion object {

        /**
         * Create new instance of [VertexBuffer].
         *
         * @param mode Mode for drawing this vertex buffer.
         * @param format Format of vertices for this vertex buffer.
         * @param buffer Buffer for writing vertex data.
         * @param vertices Count of vertices in the buffer.
         *
         * @return New instance of [VertexBuffer].
         */
        @JvmStatic
        fun of(mode: DrawerMode, format: VertexFormat, buffer: ByteBuffer, vertices: Int): VertexBuffer {
            return single<Factory>().create(mode, format, buffer, vertices)
        }

    }

}