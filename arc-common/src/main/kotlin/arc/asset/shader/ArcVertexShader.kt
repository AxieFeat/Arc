package arc.asset.shader

import arc.assets.shader.VertexShader
import java.io.File

internal data class ArcVertexShader(
    override val file: File
) : VertexShader {

    object Factory : VertexShader.Factory {
        override fun create(file: File): VertexShader {
            return ArcVertexShader(file)
        }
    }

}