package arc.gl.graphics

import arc.assets.TextureAsset
import arc.graphics.Texture
import org.lwjgl.opengl.GL41
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

data class GlTexture(
    override val asset: TextureAsset,
) : Texture {

    private var id: Int = GL41.glGenTextures()

    override val width: Int
    override val height: Int

    init {
        MemoryStack.stackPush().use { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val avChannels = stack.mallocInt(1)

            // Decode texture image into a byte buffer
            val decodedImage: ByteBuffer =
                STBImage.stbi_load_from_memory(
                    ByteBuffer.wrap(asset.file.readBytes()), w, h, avChannels, 4
                ) ?: throw RuntimeException("Could not load texture image from STBImage.")

            GL41.glBindTexture(GL41.GL_TEXTURE_2D, this.id)

            GL41.glPixelStorei(GL41.GL_UNPACK_ALIGNMENT, 1)

            GL41.glTexParameteri(GL41.GL_TEXTURE_2D, GL41.GL_TEXTURE_MIN_FILTER, GL41.GL_NEAREST)
            GL41.glTexParameteri(GL41.GL_TEXTURE_2D, GL41.GL_TEXTURE_MAG_FILTER, GL41.GL_NEAREST)

            this.width = w.get()
            this.height = w.get()

            GL41.glTexImage2D(
                GL41.GL_TEXTURE_2D, 0, GL41.GL_RGBA,
                this.width,
                this.height, 0, GL41.GL_RGBA, GL41.GL_UNSIGNED_BYTE, decodedImage
            )
            GL41.glGenerateMipmap(GL41.GL_TEXTURE_2D)
            STBImage.stbi_image_free(decodedImage)
        }
    }

    override fun bind() {
        GL41.glBindTexture(GL41.GL_TEXTURE_2D, id)
    }

    override fun unbind() {
        GL41.glBindTexture(GL41.GL_TEXTURE_2D, 0)
    }

    override fun cleanup() {
        GL41.glDeleteTextures(id)
    }

    object Factory : Texture.Factory {
        override fun create(asset: TextureAsset): Texture {
            return GlTexture(asset)
        }

    }

}