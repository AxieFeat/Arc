package arc.demo

import arc.Application
import arc.ArcFactoryProvider
import arc.gl.GlApplication

fun main() {
    // Preload factories.
    ArcFactoryProvider.install()
    ArcFactoryProvider.bootstrap()

    // Select implementation by property.
    val application = if (System.getProperty("arc.application") == "vulkan") {
        //VkApplication.preload()
        //Application.find("vulkan")
        TODO()
    } else {
        GlApplication.preload()
        Application.find("opengl")
    }

    val game = Game()

    game.start(application)
}