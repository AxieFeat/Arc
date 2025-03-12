package arc.gl.texture

import arc.assets.TextureAsset
import arc.gl.graphics.GlRenderSystem
import arc.texture.Texture
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL41
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

internal data class GlTexture(
    override val asset: TextureAsset,
) : Texture {

    override var id: Int = GL41.glGenTextures()

    init {
        MemoryStack.stackPush().use { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val channels = stack.mallocInt(1)

            val buf: ByteBuffer = STBImage.stbi_load(asset.file.absolutePath, w, h, channels, 4)
                ?: throw RuntimeException("Image file [${asset.file.absolutePath}] not loaded: " + STBImage.stbi_failure_reason())

            val width = w.get()
            val height = h.get()

            generateTexture(id, width, height, buf)
            STBImage.stbi_image_free(buf)
        }
    }

    override fun bind() {
        GlRenderSystem.bindTexture(id)
    }

    override fun unbind() {
        GlRenderSystem.bindTexture(0)
    }

    override fun cleanup() {
        GL41.glDeleteTextures(id)
    }

    object Factory : Texture.Factory {
        override fun create(asset: TextureAsset): Texture {
            return GlTexture(asset)
        }
    }

    companion object {
        private fun generateTexture(id: Int,width: Int, height: Int, buf: ByteBuffer) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id)
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
            GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf
            )
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D)
        }
    }

}