package arc.demo

import arc.ArcFactoryProvider
import arc.audio.AlAudioExtension
import arc.font.SimpleFontExtension
import arc.gl.OpenGL
import arc.input.GlfwInputExtension
import arc.model.SimpleModelExtension
import arc.vk.Vulkan

fun main() {
    // Preload factories.
    ArcFactoryProvider.install()
    ArcFactoryProvider.bootstrap()

    // Select implementation by property.
    when(System.getProperty("arc.application")) {
        "opengl" -> OpenGL.preload()
        "vulkan" -> Vulkan.preload()

        else -> OpenGL.preload()
    }

    val provider = ArcFactoryProvider

    // Load extensions.
    GlfwInputExtension.bootstrap(provider)
    AlAudioExtension.bootstrap(provider)
    SimpleModelExtension.bootstrap(provider)
    SimpleFontExtension.bootstrap(provider)

    VoxelGame.start()
}