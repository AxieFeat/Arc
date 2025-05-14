package arc.graphics

import arc.Arc
import arc.annotations.TypeFactory
import arc.graphics.vertex.VertexArrayBuffer
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexConsumer
import arc.graphics.vertex.VertexFormat
import arc.util.pattern.Cleanable
import org.jetbrains.annotations.ApiStatus
import java.nio.ByteBuffer

/**
 * DrawBuffer is an interface that used for setting vertex data.
 * With this buffer you can render any polygon.
 *
 * @sample arc.sample.drawBufferSample
 */
interface DrawBuffer : VertexConsumer, Cleanable {

    /**
     * Size of buffer.
     */
    val bufferSize: Int

    /**
     * Byte buffer, where stored all vertex data. Use it only if known, what a do.
     */
    val byteBuffer: ByteBuffer

    /**
     * Count of vertices in this buffer.
     */
    val vertexCount: Int

    /**
     * Represents the drawing mode used for rendering operations within the buffer.
     */
    val mode: DrawerMode

    /**
     * Represents the format structure of the vertices used in the `DrawBuffer`.
     */
    val format: VertexFormat

    /**
     * Clear current vertex info in buffer.
     */
    fun clear()

    /**
     * Build current buffer in vertex buffer.
     */
    fun build(): VertexBuffer

    /**
     * Build current buffer in vertex array buffer.
     */
    fun buildArray(): VertexArrayBuffer

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(mode: DrawerMode, format: VertexFormat, bufferSize: Int): DrawBuffer

    }

    companion object {

        /**
         * Create new instance of [DrawBuffer].
         *
         * @param mode Draw mode.
         * @param format Vertex setting.
         * @param bufferSize Size for buffer.
         *
         * @return New instance of [DrawBuffer]
         */
        @JvmStatic
        fun of(mode: DrawerMode, format: VertexFormat, bufferSize: Int = 256): DrawBuffer {
            return Arc.factory<Factory>().create(mode, format, bufferSize)
        }

    }

}