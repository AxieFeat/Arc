package arc.graphics

import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat
import org.joml.Matrix4f

/**
 * Drawer interface for creating and managing rendering buffers.
 */
interface Drawer {

    /**
     * Create new [DrawBuffer] for rendering.
     *
     * @param mode Mode of render.
     * @param format Format of vertex.
     * @param bufferSize Size for buffer.
     *
     * @return New instance of [DrawBuffer].
     */
    fun begin(mode: DrawerMode, format: VertexFormat, bufferSize: Int = 256): DrawBuffer = DrawBuffer.of(mode, format, bufferSize)

    /**
     * Render buffer.
     *
     * @param buffer Buffer for rendering.
     */
    fun draw(buffer: VertexBuffer)

    /**
     * Render buffer.
     *
     * @param matrix Matrix for transformation (For using it create uniform with the name "modelMatrix").
     * @param buffer Buffer for rendering.
     */
    fun draw(matrix: Matrix4f, buffer: VertexBuffer)

}