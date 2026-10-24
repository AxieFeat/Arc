package arc.gles.texture

import arc.gles.graphics.GlesContextResource
import arc.texture.Texture
import org.lwjgl.opengles.GLES20.GL_TEXTURE0
import org.lwjgl.opengles.GLES20.GL_TEXTURE_2D
import org.lwjgl.opengles.GLES20.glActiveTexture
import org.lwjgl.opengles.GLES20.glBindTexture
import org.lwjgl.opengles.GLES20.glDeleteTextures
import org.lwjgl.opengles.GLES20.glGenTextures

internal open class GlesTexture : GlesContextResource(), Texture {

    final override var id: Int = glGenTextures()
        private set

    override fun bind() {
        requireCurrentContext()
        check(id != 0) { "Texture is closed" }
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
