package arc.gles.texture

import org.lwjgl.opengles.GLES20.GL_LINEAR
import org.lwjgl.opengles.GLES20.GL_RGBA
import org.lwjgl.opengles.GLES20.GL_TEXTURE_2D
import org.lwjgl.opengles.GLES20.GL_TEXTURE_MAG_FILTER
import org.lwjgl.opengles.GLES20.GL_TEXTURE_MIN_FILTER
import org.lwjgl.opengles.GLES20.GL_UNPACK_ALIGNMENT
import org.lwjgl.opengles.GLES20.GL_UNSIGNED_BYTE
import org.lwjgl.opengles.GLES20.glBindTexture
import org.lwjgl.opengles.GLES20.glGenerateMipmap
import org.lwjgl.opengles.GLES20.glPixelStorei
import org.lwjgl.opengles.GLES20.glTexImage2D
import org.lwjgl.opengles.GLES20.glTexParameteri
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