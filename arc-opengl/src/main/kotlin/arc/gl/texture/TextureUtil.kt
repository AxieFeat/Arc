package arc.gl.texture

import org.lwjgl.opengl.GL41.*
import java.nio.ByteBuffer

internal object TextureUtil {

    @JvmStatic
    fun loadRGB(id: Int, width: Int, height: Int, buf: ByteBuffer) {
        glBindTexture(GL_TEXTURE_2D, id)
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
            GL_RGBA, GL_UNSIGNED_BYTE, buf
        )
        glGenerateMipmap(GL_TEXTURE_2D)
    }

}