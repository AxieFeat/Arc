package arc.gles

import arc.Application
import arc.ArcObjectProvider
import arc.gles.graphics.vertex.GlesVertexArrayBuffer
import arc.gles.graphics.vertex.GlesVertexBuffer
import arc.gles.shader.GlesFrameBuffer
import arc.gles.shader.GlesShaderInstance
import arc.gles.shader.GlesUniformBuffer
import arc.gles.texture.GlesTextureLoader
import arc.gles.window.EglGlesWindow
import arc.graphics.vertex.VertexArrayBuffer
import arc.graphics.vertex.VertexBuffer
import arc.shader.FrameBuffer
import arc.shader.ShaderInstance
import arc.shader.UniformBuffer
import arc.texture.TextureAtlasLoader
import arc.texture.TextureLoader
import arc.util.provider.register
import arc.window.Window

internal object GlesFactoryProvider {

    private val provider = ArcObjectProvider

    @JvmStatic
    fun bootstrap(overwrite: Boolean = false) {
        try {
            provider.register<Application.Provider>(GlesApplication.Provider, overwrite)

            provider.register<Window.Factory>(EglGlesWindow.Factory)

            provider.register<TextureLoader.Factory>(GlesTextureLoader.Factory, overwrite)
            provider.register<TextureAtlasLoader.Factory>(GlesTextureLoader.Factory, overwrite)

            provider.register<VertexBuffer.Factory>(GlesVertexBuffer.Factory, overwrite)
            provider.register<VertexArrayBuffer.Factory>(GlesVertexArrayBuffer.Factory, overwrite)

            provider.register<FrameBuffer.Factory>(GlesFrameBuffer.Factory, overwrite)
            provider.register<ShaderInstance.Factory>(GlesShaderInstance.Factory, overwrite)
            provider.register<UniformBuffer.Factory>(GlesUniformBuffer.Factory, overwrite)
        } catch (e: IllegalStateException) {
            throw RuntimeException("Can not initialize OpenGL Application, because some application already initialized!", e)
        }
    }
}
