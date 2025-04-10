package arc.graphics

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexConsumer
import arc.graphics.vertex.VertexFormat
import arc.util.pattern.Builder
import arc.util.pattern.Cleanable
import org.jetbrains.annotations.ApiStatus
import java.nio.ByteBuffer

/**
 * DrawBuffer is an interface that used for setting vertex data.
 * With this buffer you can render any polygon.
 *
 * @sample arc.sample.drawBufferSample
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface DrawBuffer : VertexConsumer, Builder<VertexBuffer>, Cleanable {

    /**
     * Size of buffer.
     */
    @get:JvmName("bufferSize")
    val bufferSize: Int

    /**
     * Byte buffer, where stored all vertex data. Use it only if known, what a do.
     */
    @get:JvmName("byteBuffer")
    val byteBuffer: ByteBuffer

    /**
     * Count of vertices in this buffer.
     */
    @get:JvmName("vertexCount")
    val vertexCount: Int

    /**
     * Represents the drawing mode used for rendering operations within the buffer.
     */
    @get:JvmName("mode")
    val mode: DrawerMode

    /**
     * Represents the format structure of the vertices used in the `DrawBuffer`.
     */
    @get:JvmName("format")
    val format: VertexFormat

    /**
     * Clear current vertex info in buffer.
     */
    fun clear()

    /**
     * Build current buffer in vertex buffer.
     */
    override fun build(): VertexBuffer

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create new instance of [DrawBuffer].
         *
         * @param mode Draw mode.
         * @param format Vertex setting.
         * @param bufferSize Size for buffer.
         *
         * @return New instance of [DrawBuffer]
         */
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
        fun create(mode: DrawerMode, format: VertexFormat, bufferSize: Int = 256): DrawBuffer {
            return Arc.factory<Factory>().create(mode, format, bufferSize)
        }

    }

}