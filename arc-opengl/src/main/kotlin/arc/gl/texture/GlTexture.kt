package arc.gl.texture

import arc.asset.TextureAsset
import arc.gl.graphics.GlRenderSystem
import arc.texture.EmptyTexture
import arc.texture.Texture
import org.lwjgl.opengl.GL41.*
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

internal data class GlTexture(
    override val asset: TextureAsset,
) : Texture {

    override val id: Int = glGenTextures()

    init {
        MemoryStack.stackPush().use { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val channels = stack.mallocInt(1)

            val buf: ByteBuffer = STBImage.stbi_load(asset.file.absolutePath, w, h, channels, 4)
                ?: throw RuntimeException("Image file [${asset.file.absolutePath}] not loaded: " + STBImage.stbi_failure_reason())

            val width = w.get()
            val height = h.get()

            TextureUtil.loadRGB(id, width, height, buf)
            STBImage.stbi_image_free(buf)
        }
    }

    override fun bind() {
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, id)
    }

    override fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
        GlRenderSystem.texture = EmptyTexture
    }

    override fun cleanup() {
        glDeleteTextures(id)
    }

    object Factory : Texture.Factory {
        override fun create(asset: TextureAsset): Texture {
            return GlTexture(asset)
        }
    }
}