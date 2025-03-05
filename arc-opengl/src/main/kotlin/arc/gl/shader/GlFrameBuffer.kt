package arc.gl.shader

import arc.gl.GlHelper
import arc.gl.graphics.GlDrawer
import arc.gl.graphics.GlRenderSystem
import arc.gl.texture.TextureUtil
import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.shader.FrameBuffer
import arc.util.Color
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL41
import java.nio.ByteBuffer

internal data class GlFrameBuffer(
    override val width: Int,
    override val height: Int,
    override var useDepth: Boolean
) : FrameBuffer {

    override var textureWidth: Int = width
    override var textureHeight: Int = height

    private var framebufferObject: Int = GlHelper.glGenFramebuffers()
    private var framebufferTexture: Int = TextureUtil.glGenTextures()
    private var depthBuffer: Int = -1
    private var framebufferFilter: Int = 0
    private val framebufferColor: FloatArray = floatArrayOf(1.0f, 1.0f, 1.0f, 0.0f)

    init {
        if (this.useDepth) {
            this.depthBuffer = GlHelper.glGenRenderbuffers()
        }

        this.setFramebufferFilter(9728)
        GlRenderSystem.bindTexture(this.framebufferTexture)
        GL41.glTexImage2D(
            GL41.GL_TEXTURE_2D, 0, GL41.GL_RGBA8,
            this.textureWidth,
            this.textureHeight, 0, GL41.GL_RGBA, GL41.GL_UNSIGNED_BYTE, (null as ByteBuffer?)
        )
        GlHelper.glBindFramebuffer(GlHelper.GL_FRAMEBUFFER, this.framebufferObject)
        GlHelper.glFramebufferTexture2D(
            GlHelper.GL_FRAMEBUFFER, GlHelper.GL_COLOR_ATTACHMENT0, 3553,
            this.framebufferTexture, 0
        )

        if (this.useDepth) {
            GlHelper.glBindRenderbuffer(GlHelper.GL_RENDERBUFFER, this.depthBuffer)
            GlHelper.glRenderbufferStorage(
                GlHelper.GL_RENDERBUFFER,
                33190,
                this.textureWidth,
                this.textureHeight,
            )
            GlHelper.glFramebufferRenderbuffer(
                GlHelper.GL_FRAMEBUFFER, GlHelper.GL_DEPTH_ATTACHMENT, GlHelper.GL_RENDERBUFFER,
                this.depthBuffer
            )
        }

        this.clear()
        this.unbindTexture()
    }

    override fun bind(viewport: Boolean) {
        GlHelper.glBindFramebuffer(GlHelper.GL_FRAMEBUFFER, this.framebufferObject)

        if (viewport) {
            GlRenderSystem.setViewport(0, 0, this.width, this.height)
        }
    }

    override fun unbind() {
        GlHelper.glBindFramebuffer(GlHelper.GL_FRAMEBUFFER, 0)
    }

    override fun bindTexture() {
        GlRenderSystem.bindTexture(this.framebufferTexture)
    }

    override fun unbindTexture() {
        GlRenderSystem.bindTexture(0)
    }

    override fun setColor(red: Float, green: Float, blue: Float, alpha: Float) {
        framebufferColor[0] = red
        framebufferColor[1] = green
        framebufferColor[2] = blue
        framebufferColor[3] = alpha
    }

    override fun delete() {
        this.unbindTexture()
        this.unbind()

        if (this.depthBuffer > -1) {
            GlHelper.glDeleteRenderbuffers(this.depthBuffer)
            this.depthBuffer = -1
        }

        if (this.framebufferTexture > -1) {
            TextureUtil.deleteTexture(this.framebufferTexture)
            this.framebufferTexture = -1
        }

        if (this.framebufferObject > -1) {
            GlHelper.glBindFramebuffer(GlHelper.GL_FRAMEBUFFER, 0)
            GlHelper.glDeleteFramebuffers(this.framebufferObject)
            this.framebufferObject = -1
        }
    }

    override fun render(width: Int, height: Int) {
        GlRenderSystem.colorMask(red = true, green = true, blue = true, alpha = false)
        GlRenderSystem.disableDepthTest()
        GlRenderSystem.depthMask(false)

        GlRenderSystem.matrixMode(GL41.GL_PROJECTION)
        GlRenderSystem.loadIdentity()


        GlRenderSystem.ortho(0.0, width.toDouble(), height.toDouble(), 0.0, 1000.0, 3000.0)

        GlRenderSystem.matrixMode(GL41.GL_MODELVIEW)
        GlRenderSystem.loadIdentity()

        GlRenderSystem.translate(0.0f, 0.0f, -2000.0f)
        GlRenderSystem.setViewport(0, 0, width, height)

        GlRenderSystem.enableTexture2D()
        GlRenderSystem.disableLighting()
        GlRenderSystem.disableAlpha()

        GlRenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        bindTexture()

        val f: Float = width.toFloat()
        val f1: Float = height.toFloat()
        val f2: Float = this.width.toFloat() / this.textureWidth.toFloat()
        val f3: Float = this.height.toFloat() / this.textureHeight.toFloat()

        val buffer = DrawBuffer.create(DrawerMode.QUADS, format)

        buffer.addVertex(0f, f1, 0f).setTexture(0, 0).setColor(Color.WHITE)
        buffer.addVertex(f, f1, 0f).setTexture(f1.toInt(), 0).setColor(Color.WHITE)
        buffer.addVertex(f, 0f, 0f).setTexture(f2.toInt(), f3.toInt()).setColor(Color.WHITE)
        buffer.addVertex(0f, 0f, 0f).setTexture(0, f3.toInt()).setColor(Color.WHITE)
        buffer.end()

        GlDrawer.draw(buffer)

        unbindTexture()

        GlRenderSystem.depthMask(true)
        GlRenderSystem.colorMask(red = true, green = true, blue = true, alpha = true)
    }

    private fun clear() {
        this.bind(true)
        GlRenderSystem.clearColor(
            this.framebufferColor[0],
            this.framebufferColor[1],
            this.framebufferColor[2],
            this.framebufferColor[3]
        )
        var i = 16384

        if (this.useDepth) {
            GlRenderSystem.clearDepth(1.0)
            i = i or 256
        }

        GlRenderSystem.clear(i)
        this.unbindTexture()
    }

    private fun setFramebufferFilter(filter: Int) {
        this.framebufferFilter = filter
        GlRenderSystem.bindTexture(this.framebufferTexture)
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter.toFloat())
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter.toFloat())
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10496.0f)
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10496.0f)
        GlRenderSystem.bindTexture(0)
    }

    object Factory : FrameBuffer.Factory {
        override fun create(width: Int, height: Int, useDepth: Boolean): FrameBuffer {
            return GlFrameBuffer(width, height, useDepth)
        }
    }

    companion object {
        private val format = VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.UV0)
            .add(VertexFormatElement.COLOR)
            .build()
    }

}