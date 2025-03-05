package arc.gl.asset

import arc.assets.shader.ShaderData
import java.io.File

internal data class GlShaderData(
    override val file: File
) : ShaderData {

    object Factory : ShaderData.Factory {
        override fun create(file: File): ShaderData {
            return GlShaderData(file)
        }
    }

}