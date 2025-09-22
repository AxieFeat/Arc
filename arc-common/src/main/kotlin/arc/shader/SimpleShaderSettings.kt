package arc.shader

internal data class SimpleShaderSettings(
    override val uniforms: List<String>,
    override val blendMode: BlendMode
) : ShaderSettings {

    object Factory : ShaderSettings.Factory {

        override fun create(uniforms: List<String>, blendMode: BlendMode): ShaderSettings {
            return SimpleShaderSettings(uniforms, blendMode)
        }
    }
}
