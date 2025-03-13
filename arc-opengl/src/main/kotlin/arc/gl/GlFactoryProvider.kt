package arc.gl

import arc.Application
import arc.ArcFactoryProvider
import arc.assets.TextureAsset
import arc.assets.shader.ShaderData
import arc.gl.asset.GlTextureAsset
import arc.gl.asset.GlShaderData
import arc.gl.graphics.GlDrawBuffer
import arc.gl.shader.GlShaderInstance
import arc.gl.texture.GlTexture
import arc.gl.texture.GlTextureAtlas
import arc.graphics.DrawBuffer
import arc.graphics.Scene
import arc.shader.ShaderInstance
import arc.texture.Texture
import arc.register
import arc.texture.TextureAtlas
import java.awt.Frame

internal object GlFactoryProvider {

    private val provider = ArcFactoryProvider

    @JvmStatic
    fun bootstrap() {
        provider.register<Application.Factory>(GlApplication.Factory)

        provider.register<TextureAsset.Factory>(GlTextureAsset.Factory)
        provider.register<Texture.Factory>(GlTexture.Factory)
        provider.register<TextureAtlas.Factory>(GlTextureAtlas.Factory)

        provider.register<ShaderInstance.Factory>(GlShaderInstance.Factory)
        provider.register<DrawBuffer.Factory>(GlDrawBuffer.Factory)
        provider.register<ShaderData.Factory>(GlShaderData.Factory)
    }
}