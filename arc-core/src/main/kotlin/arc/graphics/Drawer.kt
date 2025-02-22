package arc.graphics

import arc.graphics.vertex.VertexFormat

/**
 * Drawer interface for creating and managing rendering buffers.
 */
interface Drawer {

    /**
     * Begins a new drawing operation with the specified drawing mode and vertex format.
     *
     * @param mode The drawing mode to use, defining how primitives are connected and rendered.
     * @param format The vertex format specifying the structure of vertex data for the drawing operation.
     *
     * @return A new instance of [DrawBuffer] configured with the specified mode and format, used for executing rendering commands.
     */
    fun begin(mode: DrawerMode, format: VertexFormat): DrawBuffer

}