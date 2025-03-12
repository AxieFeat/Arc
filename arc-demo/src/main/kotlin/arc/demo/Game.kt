package arc.demo

import arc.Application
import arc.Configuration
import arc.assets.shader.FragmentShader
import arc.assets.shader.ShaderData
import arc.assets.shader.VertexShader
import arc.demo.bind.EscBind
import arc.demo.bind.KeyLogger
import arc.demo.bind.MultiBind
import arc.demo.bind.ScrollBind
import arc.files.classpath
import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.profiler.*
import arc.shader.ShaderInstance
import arc.util.Color
import arc.window.WindowHandler
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


/**
 * This own game main class.
 */
object Game : WindowHandler {

    val application: Application = Application.find()
    private val profiler: Profiler = Profiler.create().also { setProfilerContext(it) }

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

        debug() // Debug printer for profiler.

        val scene = MainScene(application)
        renderSystem.setScene(scene)

        while (!application.window.shouldClose()) {
            begin("render")

            section("beginFrame") {
                renderSystem.beginFrame()
            }

            begin("scene")

            renderSystem.scene.render() // Render our scene.

            endAndBegin("endFrame")
            renderSystem.endFrame()
            end("endFrame")

            end("render")

            end() // End root section in profiler.
        }
    }

    private fun debug() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate({
            println(profiler.root.result)
        }, 0, 10, TimeUnit.SECONDS)
    }

}