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
//        val shader = ShaderInstance.of(
//            VertexShader.from(classpath("arc/shader/position_color/position_color.vsh")),
//            FragmentShader.from(classpath("arc/shader/position_color/position_color.fsh")),
//            ShaderData.from(classpath("arc/shader/position_color/position_color.json"))
//        )
//        shader.compileShaders()

//        val texture = Texture.from(
//            TextureAsset.from(
//                classpath("arc/texture/mojang.png"),
//            )
//        )

//        val frameBuffer = FrameBuffer.create(
//            application.window.width,
//            application.window.height,
//            false
//        )

        val format = VertexFormat.builder() // Configure vertex format.
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.COLOR)
            .build()

        val renderSystem = application.renderSystem
        val drawer = renderSystem.drawer // Get drawer of application.

        while (!application.window.shouldClose()) {
            renderSystem.beginFrame()

//            shader.bind()
//            texture.bind()

//            frameBuffer.bind(false)

            val buffer = drawer.begin( // Create new buffer for writing vertex data.
                DrawerMode.TRIANGLE_STRIP, // TODO Why DrawerMode.QUADS not work with quads...?
                format
            )

            // Write values to buffer.
            buffer.addVertex(-0.5f, -0.5f, 0f).setColor(Color.RED)
            buffer.addVertex(0.5f, -0.5f, 0f).setColor(Color.AQUA)
            buffer.addVertex(-0.5f, 0.5f, 0f).setColor(Color.YELLOW)
            buffer.addVertex(0.5f, 0.5f, 0f).setColor(Color.GREEN)
            buffer.end() // End writing in buffer.

            renderSystem.rotate(1f, 0.5f, 0.5f, 0.5f)

            // Draw this buffer via drawer.
            drawer.draw(buffer)

//            frameBuffer.unbind()

//            application.renderSystem.rotate(0f, 0f, 0f, 0f)

//            frameBuffer.render(
//                application.window.width,
//                application.window.height,
//            )

//            texture.unbind()
//            shader.unbind()

            application.renderSystem.endFrame()
        }
    }

}