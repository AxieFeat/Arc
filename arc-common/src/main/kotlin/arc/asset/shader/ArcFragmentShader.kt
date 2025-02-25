package arc.asset.shader

import arc.assets.shader.FragmentShader
import java.io.File

internal data class ArcFragmentShader(
    override val file: File
) : FragmentShader {

    object Factory : FragmentShader.Factory {
        override fun create(file: File): FragmentShader {
            return ArcFragmentShader(file)
        }
    }

}