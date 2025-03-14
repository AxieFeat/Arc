package arc.gl.shader

import arc.gl.graphics.GlDrawBuffer
import arc.gl.graphics.GlDrawer
import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.shader.FrameBuffer
import arc.util.Color
import org.lwjgl.opengl.GL32.*
import org.lwjgl.system.MemoryUtil

internal class GlFrameBuffer(
    override val width: Int,
    override val height: Int,
    override var useDepth: Boolean
) : FrameBuffer {

    private val fboId: Int = glGenFramebuffers()
    private val textureId: Int
    private val depthBufferId: Int

    override val textureWidth: Int = width
    override val textureHeight: Int = height

    init {
        glBindFramebuffer(GL_FRAMEBUFFER, fboId)

        textureId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, MemoryUtil.NULL)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureId, 0)

        depthBufferId = if (useDepth) {
            val rboId = glGenRenderbuffers()
            glBindRenderbuffer(GL_RENDERBUFFER, rboId)
            glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, width, height)
            glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboId)
            rboId
        } else {
            0
        }

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw RuntimeException("Framebuffer is not complete!")
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    override fun bind(viewport: Boolean) {
        glBindFramebuffer(GL_FRAMEBUFFER, fboId)
        if (viewport) {
            glViewport(0, 0, width, height)
        }
    }

    override fun unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    override fun bindTexture() {
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureId)
    }

    override fun unbindTexture() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun setColor(red: Float, green: Float, blue: Float, alpha: Float) {
        glClearColor(red, green, blue, alpha)
    }

    override fun delete() {
        glDeleteFramebuffers(fboId)
        glDeleteTextures(textureId)
        if (useDepth) {
            glDeleteRenderbuffers(depthBufferId)
        }
    }

    override fun render(width: Int, height: Int) {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glViewport(0, 0, width, height)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        bindTexture()

        GlDrawer.draw(buffer)
    }

    override fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or (if (useDepth) GL_DEPTH_BUFFER_BIT else 0))
    }

    companion object {
        private val vertexFormat = VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.UV0)
            .build()

        private val buffer: DrawBuffer = GlDrawBuffer(DrawerMode.TRIANGLES, vertexFormat, 256).apply {
            addVertex(-1f, 1f, 0f).setTexture(0f, 1f).endVertex()
            addVertex(-1f, -1f, 0f).setTexture(0f, 0f).endVertex()
            addVertex(1f, -1f, 0f).setTexture(1f, 0f).endVertex()

            addVertex(-1f, 1f, 0f).setTexture(0f, 1f).endVertex()
            addVertex(1f, -1f, 0f).setTexture(1f, 0f).endVertex()
            addVertex(1f, 1f, 0f).setTexture(1f, 1f).endVertex()

            end()
        }
    }

    object Factory : FrameBuffer.Factory {
        override fun create(width: Int, height: Int, useDepth: Boolean): FrameBuffer {
            return GlFrameBuffer(width, height, useDepth)
        }
    }

}