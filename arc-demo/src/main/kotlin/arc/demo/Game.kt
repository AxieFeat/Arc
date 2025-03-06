package arc.demo

import arc.Application
import arc.Configuration
import arc.demo.bind.EscBind
import arc.demo.bind.KeyLogger
import arc.demo.bind.MultiBind
import arc.demo.bind.ScrollBind
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.util.Color
import arc.window.WindowHandler


/**
 * This own game main class.
 */
class Game : WindowHandler {

    private val application: Application = Application.find()

    private val quadFormat =  VertexFormat.builder() // Configure vertex format.
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.COLOR)
        .build()

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
    }

    // Infinity game loop.
    private fun loop() {
        val renderSystem = application.renderSystem

        while (!application.window.shouldClose()) {
            renderSystem.beginFrame()

//            renderSystem.rotate(1f, 0.5f, 0.5f, 0.5f)

            quad(0, 0, 350, 350, Color.GREEN)

            application.renderSystem.endFrame()
        }
    }

    private fun quad(x0: Int, y0: Int, x1: Int, y1: Int, color: Color) {
        val buffer = application.renderSystem.drawer.begin(
            DrawerMode.QUADS,
            quadFormat
        )

        val height = application.window.height
        val width = application.window.width

        // Write values to buffer.
        buffer.addVertex(x0.normalize(width), y1.normalize(height), 0f).setColor(color)
        buffer.addVertex(x1.normalize(width), y1.normalize(height), 0f).setColor(color)
        buffer.addVertex(x1.normalize(width), y0.normalize(height), 0f).setColor(color)
        buffer.addVertex(x0.normalize(width), y0.normalize(height), 0f).setColor(color)
        buffer.end() // End writing in buffer.

        // Draw this buffer via drawer.
        application.renderSystem.drawer.draw(buffer)
    }

    private fun Int.normalize(maxResolution: Int): Float {
        return (this.toFloat() / (maxResolution / 2)) - 1f
    }

}