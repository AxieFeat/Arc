package arc.gl.shader

import arc.assets.shader.UniformAsset
import arc.shader.ShaderUniforms
import arc.shader.Uniform

internal data class GlShaderUniforms(
    override val asset: UniformAsset
) : ShaderUniforms {

    override val uniforms: List<Uniform> = listOf()

    object Factory : ShaderUniforms.Factory {
        override fun create(asset: UniformAsset): ShaderUniforms {
            return GlShaderUniforms(asset)
        }

    }

}