package arc.vk

import arc.Application
import arc.ArcObjectProvider
import arc.util.provider.register
import arc.vk.window.GlfwVkWindow
import arc.window.Window

internal object VkFactoryProvider {

    private val provider = ArcObjectProvider

    @JvmStatic
    fun bootstrap(overwrite: Boolean = false) {
        try {
            provider.register<Application.Provider>(VkApplication.Provider, overwrite)

            provider.register<Window.Factory>(GlfwVkWindow.Factory)

//            provider.register<TextureLoader.Factory>(VkTextureLoader.Factory, overwrite)
//            provider.register<TextureAtlasLoader.Factory>(VkTextureLoader.Factory, overwrite)
//
//            provider.register<VertexBuffer.Factory>(VkVertexBuffer.Factory, overwrite)
//            provider.register<VertexArrayBuffer.Factory>(VkVertexArrayBuffer.Factory, overwrite)
//
//            provider.register<FrameBuffer.Factory>(VkFrameBuffer.Factory, overwrite)
//            provider.register<ShaderInstance.Factory>(VkShaderInstance.Factory, overwrite)
        } catch (e: IllegalStateException) {
            throw RuntimeException("Can not initialize Vulkan Application, because some application already initialized!", e)
        }
    }
}
