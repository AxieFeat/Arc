package arc.demo

import arc.ArcObjectProvider
import arc.gles.OpenGLES
import arc.audio.AlAudioExtension
import arc.font.SimpleFontExtension
import arc.gl.OpenGL
import arc.input.GlfwInputExtension
import arc.model.SimpleModelExtension
import arc.vk.Vulkan

fun main() {
    // Preload factories.
    ArcObjectProvider.install()
    ArcObjectProvider.bootstrap()

    // Select implementation by property.
    when(System.getProperty("arc.application")) {
        "opengl" -> OpenGL.preload()
        "vulkan" -> Vulkan.preload()

        else -> OpenGLES.preload()
    }

    val provider = ArcObjectProvider

    // Load extensions.
    GlfwInputExtension.bootstrap(provider)
    AlAudioExtension.bootstrap(provider)
    SimpleModelExtension.bootstrap(provider)
    SimpleFontExtension.bootstrap(provider)

    VoxelGame.start()
}