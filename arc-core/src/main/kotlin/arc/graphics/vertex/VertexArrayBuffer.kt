package arc.graphics.vertex

import arc.Arc.single
import arc.annotations.TypeFactory
import arc.graphics.DrawerMode
import org.jetbrains.annotations.ApiStatus
import java.nio.ByteBuffer

/**
 * This interface represents an extended vertex buffer
 * that not only stores vertex data but also manages vertex attribute layout
 * required for rendering.
 *
 * Compared to a simple [VertexBuffer], it additionally encapsulates
 * information about how vertex attributes are interpreted during rendering.
 *
 * @see VertexBuffer
 */
interface VertexArrayBuffer : VertexBuffer {

    /**
     * ID of the vertex attribute binding object in the render system.
     */
    val bindingId: Int

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(mode: DrawerMode, format: VertexFormat, buffer: ByteBuffer, vertices: Int): VertexArrayBuffer

    }

    companion object {

        /**
         * Create a new instance of [VertexArrayBuffer].
         *
         * @param mode Mode for drawing this vertex buffer.
         * @param format Format of vertices for this vertex buffer.
         * @param buffer Buffer for writing vertex data.
         * @param vertices Count of vertices in the buffer.
         *
         * @return New instance of [VertexArrayBuffer].
         */
        @JvmStatic
        fun of(mode: DrawerMode, format: VertexFormat, buffer: ByteBuffer, vertices: Int): VertexArrayBuffer {
            return single<Factory>().create(mode, format, buffer, vertices)
        }

    }

}
