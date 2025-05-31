package arc.gl.display

import arc.Application
import arc.display.Display
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.input.mouse.VirtualMouseInput
import arc.shader.FrameBuffer
import arc.shader.ShaderInstance
import org.joml.Matrix4f

internal data class GlDisplay(
    override val width: Int,
    override val height: Int
) : Display {

    private val application = Application.find()

    override val matrix: Matrix4f = Matrix4f()

    override val mouse: VirtualMouseInput = VirtualMouseInput.create(this)

    private val frameBuffer = FrameBuffer.create(
        width = width,
        height = height,
        useDepth = true
    )

    private val format = VertexFormat.builder()
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.UV)
        .build()

    private val buffer = application.renderSystem.drawer.begin(DrawerMode.TRIANGLES, format, 256).use { buffer ->
        val minX = 0f
        val minY = 0f
        val maxX = 20f
        val maxY = 10f
        val z = 0f

        buffer.addVertex(minX, maxY, z).setTexture(0f, 1f) // top-left
        buffer.addVertex(minX, minY, z).setTexture(0f, 0f) // bottom-left
        buffer.addVertex(maxX, minY, z).setTexture(1f, 0f) // bottom-right

        buffer.addVertex(minX, maxY, z).setTexture(0f, 1f) // top-left
        buffer.addVertex(maxX, minY, z).setTexture(1f, 0f) // bottom-right
        buffer.addVertex(maxX, maxY, z).setTexture(1f, 1f) // top-right

        return@use buffer.build()
    }

    override fun render(shader: ShaderInstance) {
        shader.bind()

//        glBindFramebuffer(GL_FRAMEBUFFER, 0)
//        glViewport(0, 0, width, height)
//        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        frameBuffer.bindTexture()

        application.renderSystem.drawer.draw(buffer)

        frameBuffer.unbindTexture()

        shader.unbind()
    }

    override fun clear() {
        frameBuffer.clear()
    }

    override fun bind() {
        frameBuffer.bind(false)
    }

    override fun unbind() {
        frameBuffer.unbind()
    }

    override fun cleanup() {
        frameBuffer.cleanup()
    }

    object Factory : Display.Factory {
        override fun create(width: Int, height: Int): Display {
            return GlDisplay(width, height)
        }

    }

}