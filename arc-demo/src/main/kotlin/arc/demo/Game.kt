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
import arc.profiler.*
import arc.util.Color
import arc.window.WindowHandler


/**
 * This own game main class.
 */
class Game : WindowHandler {

    private val application: Application = Application.find()
    private val profiler: Profiler = Profiler.create().also { setProfilerContext(it) }

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
            begin("render")

            section("beginFrame") {
                renderSystem.beginFrame()
            }

            begin("quad")
            quad(Color.GREEN)
            end("quad")

            begin("endFrame")
            renderSystem.endFrame()
            end("endFrame")

            end("render")

            val result = profiler.end()
            println(result)
        }
    }

    private fun quad(color: Color) {
        val buffer = application.renderSystem.drawer.begin(
            DrawerMode.QUADS,
            quadFormat
        )

        // Write values to buffer.
        buffer.addVertex(-1f, 1f, 0f).setColor(color)
        buffer.addVertex(1f, 1f, 0f).setColor(color)
        buffer.addVertex(1f, 0f, 0f).setColor(color)
        buffer.addVertex(-1f, 0f, 0f).setColor(color)
        buffer.end() // End writing in buffer.

        // Draw this buffer via drawer.
        application.renderSystem.drawer.draw(buffer)
    }

}