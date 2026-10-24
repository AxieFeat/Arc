package arc.gles.graphics

import arc.texture.Texture
import org.lwjgl.opengles.GLES20.GL_LINEAR
import org.lwjgl.opengles.GLES20.GL_MAX_TEXTURE_SIZE
import org.lwjgl.opengles.GLES20.GL_RGBA
import org.lwjgl.opengles.GLES20.GL_TEXTURE_2D
import org.lwjgl.opengles.GLES20.GL_TEXTURE_MAG_FILTER
import org.lwjgl.opengles.GLES20.GL_TEXTURE_MIN_FILTER
import org.lwjgl.opengles.GLES20.GL_TEXTURE_WRAP_S
import org.lwjgl.opengles.GLES20.GL_TEXTURE_WRAP_T
import org.lwjgl.opengles.GLES20.GL_UNPACK_ALIGNMENT
import org.lwjgl.opengles.GLES20.GL_UNSIGNED_BYTE
import org.lwjgl.opengles.GLES20.glBindTexture
import org.lwjgl.opengles.GLES20.glDeleteTextures
import org.lwjgl.opengles.GLES20.glGenTextures
import org.lwjgl.opengles.GLES20.glGetInteger
import org.lwjgl.opengles.GLES20.glPixelStorei
import org.lwjgl.opengles.GLES20.glTexImage2D
import org.lwjgl.opengles.GLES20.glTexParameteri
import org.lwjgl.opengles.GLES30.GL_CLAMP_TO_EDGE
import org.lwjgl.opengles.GLES30.GL_PIXEL_UNPACK_BUFFER
import org.lwjgl.opengles.GLES30.GL_RGBA8
import org.lwjgl.opengles.GLES30.GL_TEXTURE0
import org.lwjgl.opengles.GLES30.GL_UNPACK_ROW_LENGTH
import org.lwjgl.opengles.GLES30.GL_UNPACK_SKIP_PIXELS
import org.lwjgl.opengles.GLES30.GL_UNPACK_SKIP_ROWS
import org.lwjgl.opengles.GLES30.glActiveTexture
import org.lwjgl.opengles.GLES30.glBindBuffer
import org.lwjgl.system.MemoryUtil

internal class GlesRgbaTexture(width: Int, height: Int, rgba: ByteArray) : GlesContextResource(), Texture {

    override var id = 0
        private set

    init {
        require(width > 0 && height > 0 && Math.multiplyExact(Math.multiplyExact(width, height), 4) == rgba.size) {
            "RGBA texture needs positive dimensions and exactly width * height * 4 bytes"
        }
        require(width <= glGetInteger(GL_MAX_TEXTURE_SIZE) && height <= glGetInteger(GL_MAX_TEXTURE_SIZE)) {
            "RGBA texture exceeds the context texture-size limit"
        }
        try {
            GlesRenderContext.beginScopedPass(setOf(0)).use {
                id = glGenTextures()
                glActiveTexture(GL_TEXTURE0)
                glBindTexture(GL_TEXTURE_2D, id)
                glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0)
                glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
                glPixelStorei(GL_UNPACK_ROW_LENGTH, 0)
                glPixelStorei(GL_UNPACK_SKIP_ROWS, 0)
                glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0)
                val bytes = MemoryUtil.memAlloc(rgba.size).put(rgba).flip()
                try {
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, bytes)
                } finally {
                    MemoryUtil.memFree(bytes)
                }
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
            }
        } catch (failure: Throwable) {
            cleanup()
            throw failure
        }
    }

    override fun bind() {
        requireCurrentContext()
        check(id != 0) { "RGBA texture is closed" }
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, id)
    }

    override fun unbind() {
        requireCurrentContext()
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun cleanup() {
        if (id != 0) {
            if (isCurrentContext()) glDeleteTextures(id)
            id = 0
        }
    }
}
