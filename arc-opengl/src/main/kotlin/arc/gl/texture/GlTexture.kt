package arc.gl.texture

import arc.gl.graphics.GlRenderSystem
import arc.texture.EmptyTexture
import arc.texture.Texture
import org.lwjgl.opengl.GL41.*

internal open class GlTexture : Texture {

    override val id: Int = glGenTextures()

    override fun bind() {
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, id)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
    }

    override fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
        GlRenderSystem.texture = EmptyTexture
    }

    override fun cleanup() {
        glDeleteTextures(id)
    }
}