package arc.gl.texture

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import java.nio.ByteBuffer

internal object TextureUtil {

    @JvmStatic
    fun loadRGB(id: Int, width: Int, height: Int, buf: ByteBuffer) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id)
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
        GL11.glTexImage2D(
            GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,
            GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf
        )
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D)
    }

}