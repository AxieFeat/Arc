package arc.gl

import arc.Application
import arc.ArcFactoryProvider
import arc.display.Display
import arc.gl.display.GlDisplay
import arc.gl.model.GlModelHandler
import arc.gl.graphics.vertex.GlVertexBuffer
import arc.gl.shader.GlBlendMode
import arc.gl.shader.GlFrameBuffer
import arc.gl.shader.GlShaderInstance
import arc.gl.texture.GlTexture
import arc.gl.texture.GlTextureAtlas
import arc.graphics.ModelHandler
import arc.graphics.vertex.VertexBuffer
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

        provider.register<Texture.Factory>(GlTexture.Factory)
        provider.register<TextureAtlas.Factory>(GlTextureAtlas.Factory)

        provider.register<FrameBuffer.Factory>(GlFrameBuffer.Factory)
        provider.register<ShaderInstance.Factory>(GlShaderInstance.Factory)
        provider.register<VertexBuffer.Factory>(GlVertexBuffer.Factory)
        provider.register<BlendMode.Factory>(GlBlendMode.Factory)

        provider.register<ModelHandler.Factory>(GlModelHandler.Factory)

        provider.register<Display.Factory>(GlDisplay.Factory)
    }
}