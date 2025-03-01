package arc.gl.asset

import arc.assets.shader.UniformAsset
import arc.shader.ShaderUniforms
import java.io.File

internal data class GlUniformAsset(
    override val file: File
) : UniformAsset {

    override val uniforms: ShaderUniforms = ShaderUniforms.from(this)

    object Factory : UniformAsset.Factory {
        override fun create(file: File): UniformAsset {
            return GlUniformAsset(file)
        }

    }

}