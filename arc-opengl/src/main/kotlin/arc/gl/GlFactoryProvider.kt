package arc.gl

import arc.Application
import arc.ArcFactoryProvider
import arc.gl.graphics.vertex.GlVertexArrayBuffer
import arc.gl.graphics.vertex.GlVertexBuffer
import arc.gl.shader.GlBlendMode
import arc.gl.shader.GlFrameBuffer
import arc.gl.shader.GlShaderInstance
import arc.gl.texture.GlTextureLoader
import arc.graphics.vertex.VertexArrayBuffer
import arc.graphics.vertex.VertexBuffer
import arc.shader.BlendMode
import arc.shader.FrameBuffer
import arc.shader.ShaderInstance
import arc.texture.TextureAtlasLoader
import arc.texture.TextureLoader
import arc.util.factory.register

internal object GlFactoryProvider {

    private val provider = ArcFactoryProvider

    @JvmStatic
    fun bootstrap(overwrite: Boolean = false) {
        try {
            provider.register<Application.Factory>(GlApplication.Factory, overwrite)

            provider.register<TextureLoader.Factory>(GlTextureLoader.Factory, overwrite)
            provider.register<TextureAtlasLoader.Factory>(GlTextureLoader.Factory, overwrite)

            provider.register<VertexBuffer.Factory>(GlVertexBuffer.Factory, overwrite)
            provider.register<VertexArrayBuffer.Factory>(GlVertexArrayBuffer.Factory, overwrite)

            provider.register<FrameBuffer.Factory>(GlFrameBuffer.Factory, overwrite)
            provider.register<ShaderInstance.Factory>(GlShaderInstance.Factory, overwrite)
            provider.register<BlendMode.Factory>(GlBlendMode.Factory, overwrite)
        } catch (e: IllegalStateException) {
            throw RuntimeException("Can not initialize OpenGL Application, because some application already initialized!", e)
        }
    }
}