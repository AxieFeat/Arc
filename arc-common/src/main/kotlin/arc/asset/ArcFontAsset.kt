package arc.asset

import arc.assets.FontAsset
import java.io.File

internal data class ArcFontAsset(
    override val file: File
) : FontAsset {

    object Factory : FontAsset.Factory {
        override fun create(file: File): FontAsset {
            return ArcFontAsset(file)
        }
    }

}