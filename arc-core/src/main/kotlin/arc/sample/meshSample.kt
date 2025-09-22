@file:Suppress("DuplicatedCode")
package arc.sample

import arc.Application
import arc.asset.FileAsset
import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.shader.ShaderInstance
import arc.util.Color

internal fun meshSample() {
    // Find our application.
    // (Don't forget to preload implementations and start the application)
    val application = Application.find()

    // Now we can create buffer via render system drawer.
    val buffer: DrawBuffer = application.renderSystem.drawer.begin(
        mode = DrawerMode.TRIANGLES, // We recommend using TRIANGLES. Also, do NOT use QUADS, it's deprecated.
        format = VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.COLOR)
            .build(),

        bufferSize = 256 // Its size of a buffer. DO NOT use large values unless necessary.
    )

    // Add vertex info to our buffer.
    buffer.addVertex(0f, 0.5f, 0f).setColor(Color.BLUE)
    buffer.addVertex(-0.5f, -0.5f, 0f).setColor(Color.RED)
    buffer.addVertex(0.5f, -0.5f, 0f).setColor(Color.GREEN)

    // After writing vertex data we can build it to vertex buffer.
    val vertexBuffer = buffer.build()

    // For rendering you need Vertex and Fragment shaders (This assumes you have them in files/runtime, but you can view sample for shaders).

    // We can use classpath(), absolute() and local() functions without space instance, but in this example we use instance.
    val space = application.locationSpace

    val shader = ShaderInstance.of(
        FileAsset.from(space.classpath("arc/shader/example.vsh")),
        FileAsset.from(space.classpath("arc/shader/example.fsh")),
    )
    shader.compileShaders() // Don't forget compile shaders.

    // Create a simple game loop (Just as an example, you should have your own, only one game loop).
    while (!application.window.shouldClose()) {
        application.renderSystem.beginFrame() // Begin render frame.

        shader.bind() // Binds our shader in the current context.

        // Now we can render our buffer via drawer.
        application.renderSystem.drawer.draw(vertexBuffer)

        shader.unbind() // In this case we don't need to unbind the shader, but in real games it is quite important.

        application.renderSystem.endFrame() // End render frame.
    }
}
