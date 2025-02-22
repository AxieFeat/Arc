package arc.demo

import arc.ArcFactoryProvider
import arc.gl.GlApplication

fun main() {
    // Preload factories.
    ArcFactoryProvider.install()
    ArcFactoryProvider.bootstrap()

    GlApplication.preload()

    val game = Game()

    game.start()
}