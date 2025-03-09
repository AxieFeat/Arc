package arc.graphics

import arc.graphics.vertex.VertexFormat
import arc.shader.ShaderInstance

/**
 * Drawer interface for creating and managing rendering buffers.
 */
interface Drawer {

    fun begin(mode: DrawerMode, format: VertexFormat, bufferSize: Int = 2097152): DrawBuffer

    fun draw(buffer: DrawBuffer)

}