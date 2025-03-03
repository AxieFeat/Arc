package arc.asset

import arc.assets.TextureAsset
import arc.texture.Texture
import java.io.File

@Suppress("LeakingThis")
abstract class AbstractTextureAsset(
    override val file: File
): TextureAsset {

    override val texture: Texture = Texture.from(this)

}