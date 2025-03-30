package arc.gl.texture

import arc.asset.TextureAsset
import arc.gl.graphics.GlRenderSystem
import arc.gl.texture.TextureUtil.createDirectByteBuffer
import arc.texture.EmptyTexture
import arc.texture.TextureAtlas
import org.lwjgl.opengl.GL41.*
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

internal class GlTextureAtlas(
    override val asset: TextureAsset? = null,
    private val bytes: ByteArray? = null,
) : TextureAtlas {

    override val id: Int = glGenTextures()

    override val width: Int
    override val height: Int

    init {
        MemoryStack.stackPush().use { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val channels = stack.mallocInt(1)

            val buf: ByteBuffer = if(asset != null) {
                STBImage.stbi_load(asset.file.absolutePath, w, h, channels, 4)
                    ?: throw RuntimeException("Image file [${asset.file.absolutePath}] not loaded: " + STBImage.stbi_failure_reason())
            } else {
                STBImage.stbi_load_from_memory(bytes!!.createDirectByteBuffer(), w, h, channels, 4)
                    ?: throw RuntimeException("Image file not loaded: " + STBImage.stbi_failure_reason())
            }

            this.width = w.get()
            this.height = h.get()

            TextureUtil.loadRGB(id, width, height, buf)
            STBImage.stbi_image_free(buf)
        }
    }

    override fun uv(x: Int, y: Int): Pair<Float, Float> {
        return x / width.toFloat() to y / height.toFloat()
    }

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

    object Factory : TextureAtlas.Factory {
        override fun create(asset: TextureAsset): TextureAtlas {
            return GlTextureAtlas(asset, null)
        }

        override fun create(bytes: ByteArray): TextureAtlas {
            return GlTextureAtlas(null, bytes)
        }
    }
}