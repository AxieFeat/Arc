package arc.gl

import arc.Application
import arc.ArcFactoryProvider
import arc.asset.TextureAsset
import arc.asset.shader.ShaderData
import arc.gl.asset.GlShaderData
import arc.gl.asset.GlTextureAsset
import arc.gl.graphics.GlDrawBuffer
import arc.gl.graphics.GlVertexBuffer
import arc.gl.shader.GlBlendMode
import arc.gl.shader.GlFrameBuffer
import arc.gl.shader.GlShaderInstance
import arc.gl.texture.GlTexture
import arc.gl.texture.GlTextureAtlas
import arc.graphics.DrawBuffer
import arc.graphics.VertexBuffer
import arc.register
import arc.shader.BlendMode
import arc.shader.FrameBuffer
import arc.shader.ShaderInstance
import arc.texture.Texture
import arc.texture.TextureAtlas

internal object GlFactoryProvider {

    private val provider = ArcFactoryProvider

    @JvmStatic
    fun bootstrap() {
        provider.register<Application.Factory>(GlApplication.Factory)

        provider.register<TextureAsset.Factory>(GlTextureAsset.Factory)
        provider.register<Texture.Factory>(GlTexture.Factory)
        provider.register<TextureAtlas.Factory>(GlTextureAtlas.Factory)

        provider.register<FrameBuffer.Factory>(GlFrameBuffer.Factory)
        provider.register<ShaderInstance.Factory>(GlShaderInstance.Factory)
        provider.register<VertexBuffer.Factory>(GlVertexBuffer.Factory)
        provider.register<DrawBuffer.Factory>(GlDrawBuffer.Factory)
        provider.register<ShaderData.Factory>(GlShaderData.Factory)
        provider.register<BlendMode.Factory>(GlBlendMode.Factory)
    }
}