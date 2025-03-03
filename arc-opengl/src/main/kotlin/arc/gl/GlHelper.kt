package arc.gl

import org.apache.commons.io.IOUtils
import org.lwjgl.opengl.ARBFramebufferObject
import org.lwjgl.opengl.EXTFramebufferObject
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL41
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream
import javax.imageio.ImageIO

internal object GlHelper {

    const val DEFAULT_TEX = 33984
    const val GL_FRAMEBUFFER = 36160
    const val GL_COLOR_ATTACHMENT0 = 36064
    const val GL_RENDERBUFFER = 36161
    const val GL_DEPTH_ATTACHMENT = 36096

    @JvmStatic
    @Throws(IOException::class)
    fun readBufferedImage(imageStream: InputStream): BufferedImage {
        val bufferedImage: BufferedImage

        try {
            bufferedImage = ImageIO.read(imageStream)
        } finally {
            IOUtils.closeQuietly(imageStream)
        }

        return bufferedImage
    }

    @JvmStatic
    fun glGenFramebuffers(): Int {
        return GL41.glGenFramebuffers()
    }

    @JvmStatic
    fun glGenRenderbuffers(): Int {
        return GL41.glGenRenderbuffers()
    }

    @JvmStatic
    fun glBindFramebuffer(target: Int, framebufferIn: Int) {
        return GL41.glBindFramebuffer(target, framebufferIn)
    }

    @JvmStatic
    fun glFramebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: Int, level: Int) {
        return GL41.glFramebufferTexture2D(target, attachment, textarget, texture, level)
    }

    @JvmStatic
    fun glBindRenderbuffer(target: Int, renderbuffer: Int) {
        return GL41.glBindRenderbuffer(target, renderbuffer)
    }

    @JvmStatic
    fun glRenderbufferStorage(target: Int, internalFormat: Int, width: Int, height: Int) {
        return GL41.glRenderbufferStorage(target, internalFormat, width, height)
    }

    @JvmStatic
    fun glFramebufferRenderbuffer(target: Int, attachment: Int, renderBufferTarget: Int, renderBuffer: Int) {
        return GL41.glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer)
    }

    @JvmStatic
    fun glDeleteRenderbuffers(renderbuffer: Int) {
        return GL41.glDeleteRenderbuffers(renderbuffer)
    }

    @JvmStatic
    fun glDeleteFramebuffers(framebufferIn: Int) {
        return GL41.glDeleteFramebuffers(framebufferIn)
    }

}