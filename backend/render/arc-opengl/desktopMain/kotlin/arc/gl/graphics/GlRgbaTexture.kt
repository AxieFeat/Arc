package arc.gl.graphics

import arc.texture.Texture
import org.lwjgl.opengl.GL11.GL_LINEAR
import org.lwjgl.opengl.GL11.GL_MAX_TEXTURE_SIZE
import org.lwjgl.opengl.GL11.GL_RGBA
import org.lwjgl.opengl.GL11.GL_RGBA8
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER
import org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER
import org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S
import org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T
import org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT
import org.lwjgl.opengl.GL11.GL_UNPACK_ROW_LENGTH
import org.lwjgl.opengl.GL11.GL_UNPACK_SKIP_PIXELS
import org.lwjgl.opengl.GL11.GL_UNPACK_SKIP_ROWS
import org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE
import org.lwjgl.opengl.GL11.glBindTexture
import org.lwjgl.opengl.GL11.glDeleteTextures
import org.lwjgl.opengl.GL11.glGenTextures
import org.lwjgl.opengl.GL11.glGetInteger
import org.lwjgl.opengl.GL11.glPixelStorei
import org.lwjgl.opengl.GL11.glTexImage2D
import org.lwjgl.opengl.GL11.glTexParameteri
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.opengl.GL15.glBindBuffer
import org.lwjgl.opengl.GL21.GL_PIXEL_UNPACK_BUFFER
import org.lwjgl.system.MemoryUtil

internal class GlRgbaTexture(width: Int, height: Int, rgba: ByteArray) : GlContextResource(), Texture {

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
            GlRenderContext.beginScopedPass(setOf(0)).use {
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
