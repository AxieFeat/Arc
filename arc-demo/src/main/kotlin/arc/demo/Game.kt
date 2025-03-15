package arc.demo

import arc.Application
import arc.Configuration
import arc.demo.bind.EscBind
import arc.demo.bind.KeyLogger
import arc.demo.bind.MultiBind
import arc.demo.bind.ScrollBind
import arc.window.WindowHandler


/**
 * This own game main class.
 */
object Game : WindowHandler {

    val application: Application = Application.find()

    fun start(configuration: Configuration = Configuration.create()) {
        application.init(configuration)

        // Set window handler to this instance.
        application.window.handler = this

        // Set up bindings.
        application.keyboard.bindingProcessor.bind(EscBind)
        application.mouse.bindingProcessor.bind(ScrollBind)

        // Also we can create "Key-logger" - very bad thing :D
        application.keyboard.bindingProcessor.bind(KeyLogger)
        application.mouse.bindingProcessor.bind(KeyLogger)

        // Also we can add Multi-bindings
        application.keyboard.bindingProcessor.bind(MultiBind)

        loop()

        // Close application after exit from loop.
        application.close()
    }

    // Infinity game loop.
    private fun loop() {
        val renderSystem = application.renderSystem

        val scene = MainScene(application)
        renderSystem.setScene(scene)

        while (!application.window.shouldClose()) {
            renderSystem.beginFrame()

            renderSystem.scene.render() // Render our scene.

            renderSystem.endFrame()
        }
    }

}