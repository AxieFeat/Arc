package arc.demo

import arc.ArcFactoryProvider
import arc.audio.AlAudioExtension
import arc.gl.GlApplication
import arc.model.CommonModelExtension

fun main() {
    // Preload factories.
    ArcFactoryProvider.install()
    ArcFactoryProvider.bootstrap()

    // Select implementation by property.
    when(System.getProperty("arc.application")) {
        "opengl" -> GlApplication.preload()
//        "vulkan" -> VkApplication.preload()

        else -> GlApplication.preload()
    }

    // Load extensions.
    AlAudioExtension.bootstrap()
    CommonModelExtension.bootstrap()

    VoxelGame.start()
}