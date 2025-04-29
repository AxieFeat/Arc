package arc.gl

import arc.Application
import arc.ArcFactoryProvider
import arc.gl.graphics.vertex.GlVertexArrayBuffer
import arc.gl.graphics.vertex.GlVertexBuffer
import arc.gl.shader.GlBlendMode
import arc.gl.shader.GlFrameBuffer
import arc.gl.shader.GlShaderInstance
import arc.gl.texture.GlTexture
import arc.gl.texture.GlTextureAtlas
import arc.gl.texture.GlTextureLoader
import arc.graphics.vertex.VertexArrayBuffer
import arc.graphics.vertex.VertexBuffer
import arc.util.factory.register
import arc.shader.BlendMode
import arc.shader.FrameBuffer
import arc.shader.ShaderInstance
import arc.texture.Texture
import arc.texture.TextureAtlas
import arc.texture.TextureAtlasLoader
import arc.texture.TextureLoader

internal object GlFactoryProvider {

    private val provider = ArcFactoryProvider

    @JvmStatic
    fun bootstrap() {
        provider.register<Application.Factory>(GlApplication.Factory)

        provider.register<TextureLoader.Factory>(GlTextureLoader.Factory)
        provider.register<TextureAtlasLoader.Factory>(GlTextureLoader.Factory)

        provider.register<VertexBuffer.Factory>(GlVertexBuffer.Factory)
        provider.register<VertexArrayBuffer.Factory>(GlVertexArrayBuffer.Factory)

        provider.register<FrameBuffer.Factory>(GlFrameBuffer.Factory)
        provider.register<ShaderInstance.Factory>(GlShaderInstance.Factory)
        provider.register<BlendMode.Factory>(GlBlendMode.Factory)

    }
}