package arc.demo

import arc.ArcFactoryProvider
import arc.gl.GlApplication

fun main() {
    // Preload factories.
    ArcFactoryProvider.install()
    ArcFactoryProvider.bootstrap()

    // Preload implementations.
    GlApplication.preload()
    //VkApplication.preload()

    val game = Game()

    game.start()
}