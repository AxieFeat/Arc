package arc.gl.texture

import arc.gl.graphics.GlContextResource
import arc.texture.Texture
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.glBindTexture
import org.lwjgl.opengl.GL11.glDeleteTextures
import org.lwjgl.opengl.GL11.glGenTextures
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture

internal open class GlTexture : GlContextResource(), Texture {

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
