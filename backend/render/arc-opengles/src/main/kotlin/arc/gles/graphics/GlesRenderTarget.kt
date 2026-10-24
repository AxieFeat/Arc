package arc.gles.graphics

import arc.graphics.RenderTarget
import arc.texture.Texture
import org.lwjgl.opengles.GLES20.GL_LINEAR
import org.lwjgl.opengles.GLES20.GL_MAX_TEXTURE_SIZE
import org.lwjgl.opengles.GLES20.GL_RGBA
import org.lwjgl.opengles.GLES20.GL_TEXTURE_2D
import org.lwjgl.opengles.GLES20.GL_TEXTURE_MAG_FILTER
import org.lwjgl.opengles.GLES20.GL_TEXTURE_MIN_FILTER
import org.lwjgl.opengles.GLES20.GL_TEXTURE_WRAP_S
import org.lwjgl.opengles.GLES20.GL_TEXTURE_WRAP_T
import org.lwjgl.opengles.GLES20.GL_UNSIGNED_BYTE
import org.lwjgl.opengles.GLES20.glBindTexture
import org.lwjgl.opengles.GLES20.glDeleteTextures
import org.lwjgl.opengles.GLES20.glGenTextures
import org.lwjgl.opengles.GLES20.glGetInteger
import org.lwjgl.opengles.GLES20.glTexImage2D
import org.lwjgl.opengles.GLES20.glTexParameteri
import org.lwjgl.opengles.GLES30.GL_CLAMP_TO_EDGE
import org.lwjgl.opengles.GLES30.GL_COLOR_ATTACHMENT0
import org.lwjgl.opengles.GLES30.GL_DEPTH24_STENCIL8
import org.lwjgl.opengles.GLES30.GL_DEPTH_STENCIL_ATTACHMENT
import org.lwjgl.opengles.GLES30.GL_DRAW_FRAMEBUFFER_BINDING
import org.lwjgl.opengles.GLES30.GL_FRAMEBUFFER
import org.lwjgl.opengles.GLES30.GL_FRAMEBUFFER_COMPLETE
import org.lwjgl.opengles.GLES30.GL_PIXEL_UNPACK_BUFFER
import org.lwjgl.opengles.GLES30.GL_READ_FRAMEBUFFER_BINDING
import org.lwjgl.opengles.GLES30.GL_RENDERBUFFER
import org.lwjgl.opengles.GLES30.GL_RGBA8
import org.lwjgl.opengles.GLES30.GL_TEXTURE0
import org.lwjgl.opengles.GLES30.glActiveTexture
import org.lwjgl.opengles.GLES30.glBindBuffer
import org.lwjgl.opengles.GLES30.glBindFramebuffer
import org.lwjgl.opengles.GLES30.glBindRenderbuffer
import org.lwjgl.opengles.GLES30.glCheckFramebufferStatus
import org.lwjgl.opengles.GLES30.glDeleteFramebuffers
import org.lwjgl.opengles.GLES30.glDeleteRenderbuffers
import org.lwjgl.opengles.GLES30.glFramebufferRenderbuffer
import org.lwjgl.opengles.GLES30.glFramebufferTexture2D
import org.lwjgl.opengles.GLES30.glGenFramebuffers
import org.lwjgl.opengles.GLES30.glGenRenderbuffers
import org.lwjgl.opengles.GLES30.glRenderbufferStorage

internal class GlesRenderTarget(
    width: Int,
    height: Int,
    override val depthStencil: Boolean,
) : GlesContextResource(), RenderTarget {

    override var width: Int = 0
        private set
    override var height: Int = 0
        private set
    private var framebuffer = 0
    private var texture = 0
    private var depthStencilBuffer = 0
    private var closed = false
    private val owner = Thread.currentThread()

    override val colorTexture: Texture = object : Texture {
        override val resourceGeneration get() = this@GlesRenderTarget.resourceGeneration
        override val id: Int
            get() {
                checkOpen()
                return texture
            }

        override fun bind() {
            checkOpen()
            glActiveTexture(GL_TEXTURE0)
            glBindTexture(GL_TEXTURE_2D, texture)
        }

        override fun unbind() {
            checkOpen()
            glBindTexture(GL_TEXTURE_2D, 0)
        }

        // A borrowed attachment view never destroys its owning target.
        override fun cleanup() = Unit
    }

    init {
        resize(width, height)
    }

    fun bind() {
        checkOpen()
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer)
    }

    override fun resize(width: Int, height: Int): Boolean {
        checkOpen()
        require(width > 0 && height > 0) { "Render target dimensions must be positive" }
        val maximum = glGetInteger(GL_MAX_TEXTURE_SIZE)
        require(width <= maximum && height <= maximum) { "Render target exceeds maximum texture size $maximum" }
        if (width == this.width && height == this.height) return false
        check(
            framebuffer == 0 || (glGetInteger(GL_DRAW_FRAMEBUFFER_BINDING) != framebuffer &&
                    glGetInteger(GL_READ_FRAMEBUFFER_BINDING) != framebuffer)
        ) { "Unbind a render target before resizing it" }
        var newFramebuffer = 0
        var newTexture = 0
        var newDepthStencil = 0
        try {
            GlesRenderContext.beginScopedPass(textureUnits = setOf(0)).use {
                newFramebuffer = glGenFramebuffers()
                newTexture = glGenTextures()
                glBindFramebuffer(GL_FRAMEBUFFER, newFramebuffer)
                glActiveTexture(GL_TEXTURE0)
                glBindTexture(GL_TEXTURE_2D, newTexture)
                glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0)
                glTexImage2D(
                    GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0,
                    GL_RGBA, GL_UNSIGNED_BYTE, 0L
                )
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
                glFramebufferTexture2D(
                    GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                    GL_TEXTURE_2D, newTexture, 0
                )
                if (depthStencil) {
                    newDepthStencil = glGenRenderbuffers()
                    glBindRenderbuffer(GL_RENDERBUFFER, newDepthStencil)
                    glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height)
                    glFramebufferRenderbuffer(
                        GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT,
                        GL_RENDERBUFFER, newDepthStencil
                    )
                }
                check(glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE) {
                    "Render target attachments are incomplete"
                }
            }
        } catch (failure: Throwable) {
            if (newFramebuffer != 0) glDeleteFramebuffers(newFramebuffer)
            if (newTexture != 0) glDeleteTextures(newTexture)
            if (newDepthStencil != 0) glDeleteRenderbuffers(newDepthStencil)
            throw failure
        }
        deleteAttachments()
        framebuffer = newFramebuffer
        texture = newTexture
        depthStencilBuffer = newDepthStencil
        this.width = width
        this.height = height
        return true
    }

    override fun cleanup() {
        if (closed) return
        deleteAttachments()
        closed = true
    }

    private fun deleteAttachments() {
        if (isCurrentContext()) {
            if (framebuffer != 0) glDeleteFramebuffers(framebuffer)
            if (texture != 0) glDeleteTextures(texture)
            if (depthStencilBuffer != 0) glDeleteRenderbuffers(depthStencilBuffer)
        }
        framebuffer = 0
        texture = 0
        depthStencilBuffer = 0
    }

    private fun checkOpen() {
        requireCurrentContext()
        check(Thread.currentThread() === owner) { "Render target operations require its creating thread" }
        check(!closed) { "Render target is closed" }
    }
}
