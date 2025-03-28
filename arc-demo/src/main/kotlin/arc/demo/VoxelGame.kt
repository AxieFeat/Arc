package arc.demo

import arc.Application
import arc.Configuration
import arc.asset.SoundAsset
import arc.audio.Sound
import arc.demo.screen.MainMenuScreen
import arc.demo.screen.Screen
import arc.demo.shader.ShaderContainer
import arc.files.classpath
import arc.graphics.vertex.VertexFormat
import arc.window.WindowHandler

object VoxelGame : WindowHandler {

    val application: Application = Application.find()

    fun start(configuration: Configuration = Configuration.create()) {
        application.init(configuration)

        loadShaders()

        // Set window handler to this instance.
        application.window.handler = this
        application.window.isVsync = true
        setScreen(MainMenuScreen)

        val asset = SoundAsset.from(classpath("arc/sound/pigstep.ogg"))
        val sound = Sound.from(asset)

        sound.play(volume = 0.3f, loop = true)

        loop()

        // Close application after exit from loop.
        application.close()
    }

    fun setScreen(screen: Screen) {
        application.renderSystem.setScene(screen)
    }

    // Infinity game loop.
    private fun loop() {
        val renderSystem = application.renderSystem

        while (!application.window.shouldClose()) {
            renderSystem.beginFrame()

            renderSystem.scene.render() // Render our scene.

            renderSystem.endFrame()
        }
    }

    private fun loadShaders() {
        application.window.name = "Loading shaders..."

        ShaderContainer
        VertexFormat
    }

}