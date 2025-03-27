package arc.demo

import arc.Application
import arc.Configuration
import arc.demo.screen.MainMenuScreen
import arc.demo.screen.Screen
import arc.demo.shader.ShaderContainer
import arc.graphics.vertex.VertexFormat
import arc.window.WindowHandler

object VoxelGame : WindowHandler {

    val application: Application = Application.find()

    fun start(configuration: Configuration = Configuration.create()) {
        application.init(configuration)

        loadShaders()

        // Set window handler to this instance.
        application.window.handler = this
        application.window.isVsync = false
        setScreen(MainMenuScreen)

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