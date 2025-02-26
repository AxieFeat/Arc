package arc.gl.asset

import arc.assets.TextureAsset
import arc.texture.Texture
import java.io.File

internal data class GlTextureAsset(
    override val file: File
) : TextureAsset {

    override val texture: Texture = Texture.create(this)

    override fun bind(): Texture {
        texture.bind()

        return texture
    }

    override fun unbind() {
        texture.unbind()
    }

    object Factory : TextureAsset.Factory {
        override fun create(file: File): TextureAsset {
            return GlTextureAsset(file)
        }

    }
}