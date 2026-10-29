package arc.gl.shader

import arc.gl.graphics.GlRenderContext
import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.TextureSampling
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.shader.FrameBuffer
import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT
import org.lwjgl.opengl.GL11.glClear

/**
 * Legacy facade backed by the scoped target contract. render composites without clearing the destination.
 */
internal class GlFrameBuffer(width: Int, height: Int, override val isUseDepth: Boolean) : FrameBuffer {

    private val context = GlRenderContext
    private val target = context.createRenderTarget(width, height, isUseDepth)
    private val sampler = lazy { context.createSampler(TextureSampling.LINEAR) }
    private val geometry = lazy {
        val format = VertexFormat.builder().add(VertexFormatElement.POSITION).add(VertexFormatElement.UV).build()
        DrawBuffer.of(DrawerMode.TRIANGLES, format, 30).use {
            it.addVertex(-1f, 1f, 0f).setTexture(0f, 1f)
            it.addVertex(-1f, -1f, 0f).setTexture(0f, 0f)
            it.addVertex(1f, -1f, 0f).setTexture(1f, 0f)
            it.addVertex(-1f, 1f, 0f).setTexture(0f, 1f)
            it.addVertex(1f, -1f, 0f).setTexture(1f, 0f)
            it.addVertex(1f, 1f, 0f).setTexture(1f, 1f)
            it.build()
        }
    }
    private var closed = false
    override val width get() = target.width
    override val height get() = target.height
    override val textureWidth get() = target.width
    override val textureHeight get() = target.height

    override fun bind(viewport: Boolean) {
        checkOpen()
        context.bindTarget(target)
        if (viewport) context.setViewport(0, 0, width, height)
    }

    override fun unbind() {
        checkOpen()
        context.bindTarget(null)
    }

    override fun bindTexture() {
        checkOpen()
        context.bindTexture(target.colorTexture)
    }

    override fun unbindTexture() {
        checkOpen()
        target.colorTexture.unbind()
    }

    override fun setColor(red: Float, green: Float, blue: Float, alpha: Float) {
        checkOpen()
        context.clearColor(red, green, blue, alpha)
    }

    override fun render(width: Int, height: Int) {
        checkOpen()
        require(width >= 0 && height >= 0)
        if (width == 0 || height == 0) return
        context.beginScopedPass(setOf(0)).use {
            context.bindTarget(null)
            context.setViewport(0, 0, width, height)
            context.bindTexture(target.colorTexture)
            context.bindSampler(sampler.value)
            context.drawer.draw(geometry.value)
        }
    }

    override fun resize(width: Int, height: Int): Boolean {
        checkOpen()
        return target.resize(width, height)
    }

    override fun clear() {
        checkOpen()
        glClear(GL_COLOR_BUFFER_BIT or if (isUseDepth) GL_DEPTH_BUFFER_BIT else 0)
    }

    override fun cleanup() {
        if (closed) return
        if (geometry.isInitialized()) geometry.value.cleanup()
        if (sampler.isInitialized()) sampler.value.cleanup()
        target.cleanup()
        closed = true
    }

    private fun checkOpen() {
        check(!closed) { "Framebuffer is closed" }
        check(context.resourceGeneration == target.resourceGeneration) { "Framebuffer belongs to a stale or missing context" }
    }

    object Factory : FrameBuffer.Factory {

        override fun create(width: Int, height: Int, useDepth: Boolean): FrameBuffer = GlFrameBuffer(width, height, useDepth)
    }
}
