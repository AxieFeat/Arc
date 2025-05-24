package arc.vk

import arc.Application
import arc.ArcFactoryProvider
import arc.graphics.vertex.VertexArrayBuffer
import arc.graphics.vertex.VertexBuffer
import arc.shader.BlendMode
import arc.shader.FrameBuffer
import arc.shader.ShaderInstance
import arc.texture.TextureAtlasLoader
import arc.texture.TextureLoader
import arc.util.factory.register

internal object VkFactoryProvider {

    private val provider = ArcFactoryProvider

    @JvmStatic
    fun bootstrap(overwrite: Boolean = false) {
        try {
            provider.register<Application.Factory>(VkApplication.Factory, overwrite)

//            provider.register<TextureLoader.Factory>(VkTextureLoader.Factory, overwrite)
//            provider.register<TextureAtlasLoader.Factory>(VkTextureLoader.Factory, overwrite)
//
//            provider.register<VertexBuffer.Factory>(VkVertexBuffer.Factory, overwrite)
//            provider.register<VertexArrayBuffer.Factory>(VkVertexArrayBuffer.Factory, overwrite)
//
//            provider.register<FrameBuffer.Factory>(VkFrameBuffer.Factory, overwrite)
//            provider.register<ShaderInstance.Factory>(VkShaderInstance.Factory, overwrite)
//            provider.register<BlendMode.Factory>(VkBlendMode.Factory, overwrite)
        } catch (e: IllegalStateException) {
            throw RuntimeException("Can not initialize Vulkan Application, because some application already initialized!", e)
        }
    }
    
}