package arc.gles.texture

import arc.texture.Texture
import org.lwjgl.opengles.GLES20.GL_NEAREST
import org.lwjgl.opengles.GLES20.GL_TEXTURE0
import org.lwjgl.opengles.GLES20.GL_TEXTURE_2D
import org.lwjgl.opengles.GLES20.GL_TEXTURE_MAG_FILTER
import org.lwjgl.opengles.GLES20.GL_TEXTURE_MIN_FILTER
import org.lwjgl.opengles.GLES20.glActiveTexture
import org.lwjgl.opengles.GLES20.glBindTexture
import org.lwjgl.opengles.GLES20.glDeleteTextures
import org.lwjgl.opengles.GLES20.glGenTextures
import org.lwjgl.opengles.GLES20.glTexParameteri

internal open class GlesTexture : Texture {

    override val id: Int = glGenTextures()

    override fun bind() {
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, id)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
    }

    override fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun cleanup() {
        glDeleteTextures(id)
    }
}
