package arc.gl.shader

import arc.gl.graphics.GlDrawer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.shader.FrameBuffer
import org.lwjgl.opengl.GL32.*
import org.lwjgl.system.MemoryUtil

internal class GlFrameBuffer(
    override var width: Int,
    override var height: Int,
    override val isUseDepth: Boolean
) : FrameBuffer {

    private var fboId: Int = 0
    private var textureId: Int = 0
    private var depthBufferId: Int = 0

    override val textureWidth: Int = width
    override val textureHeight: Int = height

    init {
        createFramebuffer(width, height)
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

    override fun cleanup() {
        glDeleteFramebuffers(fboId)
        glDeleteTextures(textureId)
        if (isUseDepth) {
            glDeleteRenderbuffers(depthBufferId)
        }
    }

    override fun render(width: Int, height: Int) {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glViewport(0, 0, width, height)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        bindTexture()

        GlDrawer.draw(buffer)

        unbindTexture()
    }

    override fun resize(width: Int, height: Int) {
        if (width != this.width || height != this.height) {
            this.width = width
            this.height = height
            createFramebuffer(width, height)
        }
    }

    override fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or (if (isUseDepth) GL_DEPTH_BUFFER_BIT else 0))
    }

    private fun createFramebuffer(width: Int, height: Int) {
        cleanup()

        fboId = glGenFramebuffers()
        glBindFramebuffer(GL_FRAMEBUFFER, fboId)

        textureId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, MemoryUtil.NULL)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureId, 0)

        depthBufferId = if (isUseDepth) {
            val rbo = glGenRenderbuffers()
            glBindRenderbuffer(GL_RENDERBUFFER, rbo)
            glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, width, height)
            glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rbo)
            rbo
        } else {
            0
        }

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw RuntimeException("Framebuffer is not complete!")
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    companion object {
        private val vertexFormat = VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.UV)
            .build()

        private val buffer = GlDrawer.begin(DrawerMode.TRIANGLES, vertexFormat, 256).apply {
            addVertex(-1f, 1f, 0f).setTexture(0f, 1f)
            addVertex(-1f, -1f, 0f).setTexture(0f, 0f)
            addVertex(1f, -1f, 0f).setTexture(1f, 0f)

            addVertex(-1f, 1f, 0f).setTexture(0f, 1f)
            addVertex(1f, -1f, 0f).setTexture(1f, 0f)
            addVertex(1f, 1f, 0f).setTexture(1f, 1f)
        }.use { it.build() }
    }

    object Factory : FrameBuffer.Factory {
        override fun create(width: Int, height: Int, useDepth: Boolean): FrameBuffer {
            return GlFrameBuffer(width, height, useDepth)
        }
    }
}