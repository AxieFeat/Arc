package arc.shader

internal data class SimpleShaderSettings(
    override val uniforms: List<String>
) : ShaderSettings {

    object Factory : ShaderSettings.Factory {

        override fun create(uniforms: List<String>): ShaderSettings {
            return SimpleShaderSettings(uniforms)
        }
    }
}
