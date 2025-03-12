package arc.gl.texture

import arc.assets.TextureAsset
import arc.gl.GlHelper
import arc.gl.graphics.GlRenderSystem
import arc.texture.Texture
import org.lwjgl.opengl.GL41
import java.awt.image.BufferedImage
import java.io.InputStream

internal data class GlTexture(
    override val asset: TextureAsset,
) : Texture {

    override var id: Int = GL41.glGenTextures()

    init {
        val inputStream: InputStream = asset.file.inputStream()

        inputStream.use {
            val bufferedImage: BufferedImage = GlHelper.readBufferedImage(it)

            //            TextureUtil.uploadTextureImageAllocate(id, bufferedimage, flag, flag1)
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

}