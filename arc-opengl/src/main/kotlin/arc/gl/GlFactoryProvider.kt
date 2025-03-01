package arc.gl

import arc.ArcFactoryProvider
import arc.assets.TextureAsset
import arc.assets.shader.UniformAsset
import arc.gl.asset.GlTextureAsset
import arc.gl.asset.GlUniformAsset
import arc.gl.graphics.GlDrawBuffer
import arc.gl.graphics.GlScene
import arc.gl.shader.GlShaderInstance
import arc.gl.shader.GlShaderUniforms
import arc.gl.texture.GlTexture
import arc.graphics.DrawBuffer
import arc.graphics.Scene
import arc.shader.ShaderInstance
import arc.texture.Texture
import arc.register
import arc.shader.ShaderUniforms

object GlFactoryProvider {

    private val provider = ArcFactoryProvider

    @JvmStatic
    fun bootstrap() {
        provider.register<TextureAsset.Factory>(GlTextureAsset.Factory)
        provider.register<Texture.Factory>(GlTexture.Factory)

        provider.register<ShaderInstance.Factory>(GlShaderInstance.Factory)
        provider.register<DrawBuffer.Factory>(GlDrawBuffer.Factory)
        provider.register<UniformAsset.Factory>(GlUniformAsset.Factory)
        provider.register<ShaderUniforms.Factory>(GlShaderUniforms.Factory)

        provider.register<Scene.Factory>(GlScene.Factory)
    }
}