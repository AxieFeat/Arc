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

        // Close application after exit from loop.
        application.close()
    }

    // Infinity game loop.
    private fun loop() {
        val renderSystem = application.renderSystem
        val shader = ShaderInstance.of(
            VertexShader.from(classpath("arc/shader/position_color/position_color.vsh")),
            FragmentShader.from(classpath("arc/shader/position_color/position_color.fsh")),
            ShaderData.from(classpath("arc/shader/position_color/position_color.json")),
        )
        shader.compileShaders()

        debug() // Debug printer for profiler.

        val bufferForRender = createBuffer()

        while (!application.window.shouldClose()) {
            begin("render")

            section("beginFrame") {
                renderSystem.beginFrame()
            }

            begin("exampleRender")

            shader.bind()
            doRender(bufferForRender)
            shader.unbind()

            endAndBegin("endFrame")
            renderSystem.endFrame()
            end("endFrame")

            end("render")

            end() // End root section in profiler.
        }
    }

    private fun createBuffer(): DrawBuffer {
        val buffer = application.renderSystem.drawer.begin(
            DrawerMode.QUADS,
            quadFormat
        )

        // Write values to buffer.
        buffer.addVertex(0.35f, 0.5f, 0f).setColor(Color.of(226, 68, 97))
        buffer.addVertex(-0.35f, 0.5f, 0f).setColor(Color.of(127, 82, 255))
        buffer.addVertex(-0.35f, -0.5f, 0f).setColor(Color.of(149, 61, 245))
        buffer.addVertex(0.35f, -0.5f, 0f).setColor(Color.of(149, 61, 245))
        buffer.end() // End writing in buffer.

        return buffer
    }

    private fun doRender(buffer: DrawBuffer) {
//        application.renderSystem.rotate(1f, 0.5f, 0.5f, 0.5f)

        // Draw this buffer via drawer.
        application.renderSystem.drawer.draw(buffer)
    }

    private fun debug() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate({
            println(profiler.root.result)
        }, 0, 10000, TimeUnit.MILLISECONDS)
    }

}