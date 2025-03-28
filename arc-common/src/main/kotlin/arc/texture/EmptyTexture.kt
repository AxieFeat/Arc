package arc.texture

import arc.assets.TextureAsset
import java.io.File

object EmptyTexture : Texture {
    override val asset: TextureAsset = TextureAsset.from(File(""))
    override val id: Int = -1

    override fun bind() {

    }

    override fun unbind() {

    }

    override fun cleanup() {

    }

}