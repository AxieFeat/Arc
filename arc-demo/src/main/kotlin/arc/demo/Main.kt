package arc.demo

import arc.Application
import arc.ArcFactoryProvider
import arc.gl.GlApplication

fun main() {
    // Preload factories.
    ArcFactoryProvider.install()
    ArcFactoryProvider.bootstrap()

    // Select implementation by property.
    when(System.getProperty("arc.application")) {
        "opengl" -> GlApplication.preload()
        //"vulkan" -> VkApplication.preload()

        else -> GlApplication.preload()
    }

    val game = Game()

    game.start()
}