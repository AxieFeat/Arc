package arc.demo

import arc.Application
import arc.Configuration
import arc.assets.TextureAsset
import arc.assets.shader.FragmentShader
import arc.assets.shader.UniformAsset
import arc.assets.shader.VertexShader
import arc.files.classpath
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.shader.ShaderInstance
import arc.shader.ShaderUniforms
import arc.texture.Texture
import arc.util.Color
import arc.window.WindowHandler

/**
 * This own game main class.
 */
class Game : WindowHandler {

    private val application: Application = Application.find("opengl")

    fun start(configuration: Configuration = Configuration.create()) {
        application.init(configuration)

        // Set window handler to this instance.
        application.window.handler = this

        loop()
    }

    // Infinity game loop.
    private fun loop() {
        val shader = ShaderInstance.of(
            VertexShader.from(classpath("arc/shader/position_color/position_color.vsh")),
            FragmentShader.from(classpath("arc/shader/position_color/position_color.fsh")),
            ShaderUniforms.from(UniformAsset.from(classpath("arc/shader/position_color/position_color.json")))
        )
        shader.compileShaders()

//        val texture = Texture.from(
//            TextureAsset.from(
//                classpath("arc/texture/mojang.png"),
//            )
//        )

        while (!application.window.shouldClose()) {
            application.renderSystem.beginFrame()

            shader.bind()
//            texture.bind()

            val drawer = application.renderSystem.drawer // Get drawer of application.

            val buffer = drawer.begin( // Create new buffer for writing vertex data.
                DrawerMode.QUADS, // Set mode to Quads.
                VertexFormat.builder() // Configure vertex format.
                    .add(VertexFormatElement.POSITION)
                    .add(VertexFormatElement.COLOR)
                    .build()
            )

            // Write values to buffer.
            buffer.addVertex(0f, 0f, 0f).setColor(Color.RED)
            buffer.addVertex(0f, 100f, 0f).setColor(Color.YELLOW)
            buffer.addVertex(100f, 0f, 0f).setColor(Color.AQUA)
            buffer.addVertex(100f, 100f, 0f).setColor(Color.GOLD)
            buffer.end() // End writing in buffer.

            // Draw this buffer via drawer.
//            drawer.draw(buffer)

//            texture.unbind()
            shader.unbind()

            application.renderSystem.endFrame()
        }
    }

}