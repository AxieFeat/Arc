package arc.gles.shader

import arc.gles.graphics.GlesDrawer
import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.shader.FrameBuffer
import org.lwjgl.opengles.GLES20.GL_COLOR_ATTACHMENT0
import org.lwjgl.opengles.GLES20.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengles.GLES20.GL_DEPTH_ATTACHMENT
import org.lwjgl.opengles.GLES20.GL_DEPTH_BUFFER_BIT
import org.lwjgl.opengles.GLES20.GL_FRAMEBUFFER
import org.lwjgl.opengles.GLES20.GL_FRAMEBUFFER_COMPLETE
import org.lwjgl.opengles.GLES20.GL_LINEAR
import org.lwjgl.opengles.GLES20.GL_RENDERBUFFER
import org.lwjgl.opengles.GLES20.GL_RGBA
import org.lwjgl.opengles.GLES20.GL_TEXTURE0
import org.lwjgl.opengles.GLES20.GL_TEXTURE_2D
import org.lwjgl.opengles.GLES20.GL_TEXTURE_MAG_FILTER
import org.lwjgl.opengles.GLES20.GL_TEXTURE_MIN_FILTER
import org.lwjgl.opengles.GLES20.GL_UNSIGNED_BYTE
import org.lwjgl.opengles.GLES20.glActiveTexture
import org.lwjgl.opengles.GLES20.glBindFramebuffer
import org.lwjgl.opengles.GLES20.glBindRenderbuffer
import org.lwjgl.opengles.GLES20.glBindTexture
import org.lwjgl.opengles.GLES20.glCheckFramebufferStatus
import org.lwjgl.opengles.GLES20.glClear
import org.lwjgl.opengles.GLES20.glClearColor
import org.lwjgl.opengles.GLES20.glDeleteFramebuffers
import org.lwjgl.opengles.GLES20.glDeleteRenderbuffers
import org.lwjgl.opengles.GLES20.glDeleteTextures
import org.lwjgl.opengles.GLES20.glFramebufferRenderbuffer
import org.lwjgl.opengles.GLES20.glFramebufferTexture2D
import org.lwjgl.opengles.GLES20.glGenFramebuffers
import org.lwjgl.opengles.GLES20.glGenRenderbuffers
import org.lwjgl.opengles.GLES20.glGenTextures
import org.lwjgl.opengles.GLES20.glRenderbufferStorage
import org.lwjgl.opengles.GLES20.glTexImage2D
import org.lwjgl.opengles.GLES20.glTexParameteri
import org.lwjgl.opengles.GLES20.glViewport
import org.lwjgl.opengles.GLES30.GL_DEPTH_COMPONENT24
import org.lwjgl.system.MemoryUtil

internal class GlesFrameBuffer(
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

        GlesDrawer.draw(buffer)

        unbindTexture()
    }

    override fun resize(width: Int, height: Int): Boolean {
        if (width != this.width || height != this.height) {
            this.width = width
            this.height = height
            createFramebuffer(width, height)
            return true
        }
        return false
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

        check(glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE) {
            "Framebuffer is not complete!"
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    companion object {
        private val vertexFormat = VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.UV)
            .build()

        private val buffer = DrawBuffer.of(DrawerMode.TRIANGLES, vertexFormat, 256).use {
            it.addVertex(-1f, 1f, 0f).setTexture(0f, 1f)
            it.addVertex(-1f, -1f, 0f).setTexture(0f, 0f)
            it.addVertex(1f, -1f, 0f).setTexture(1f, 0f)

            it.addVertex(-1f, 1f, 0f).setTexture(0f, 1f)
            it.addVertex(1f, -1f, 0f).setTexture(1f, 0f)
            it.addVertex(1f, 1f, 0f).setTexture(1f, 1f)

            it.build()
        }
    }

    object Factory : FrameBuffer.Factory {
        override fun create(width: Int, height: Int, useDepth: Boolean): FrameBuffer {
            return GlesFrameBuffer(width, height, useDepth)
        }
    }
}
