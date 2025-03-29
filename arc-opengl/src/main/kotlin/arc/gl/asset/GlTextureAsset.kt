package arc.gl.asset

import arc.asset.TextureAsset
import java.io.File

internal data class GlTextureAsset(
    override val file: File
) : TextureAsset {

    object Factory : TextureAsset.Factory {
        override fun create(file: File): TextureAsset {
            return GlTextureAsset(file)
        }
    }
}