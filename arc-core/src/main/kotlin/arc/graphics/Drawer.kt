package arc.graphics

import arc.graphics.vertex.VertexFormat

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
    fun begin(mode: DrawerMode, format: VertexFormat, bufferSize: Int = 2097152): DrawBuffer

    /**
     * Render buffer.
     *
     * @param buffer Buffer for rendering.
     */
    fun draw(buffer: DrawBuffer)

}