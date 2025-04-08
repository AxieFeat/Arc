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
    // (Don't forget preload factories, implementation and start application)
    val application = Application.find()

    // Now we can create buffer via render system drawer.
    val buffer: DrawBuffer = application.renderSystem.drawer.begin(
        mode = DrawerMode.TRIANGLES, // We recommend use TRIANGLES. Also NOT use QUADS, its deprecated.
        format = VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.COLOR)
            .build(),

        bufferSize = 256 // Its size of buffer. DO NOT use large values unless necessary.
    )

    // Add vertex info to our buffer. Not forget end vertex via endVertex()
    buffer.addVertex(0f, 0.5f, 0f).setColor(Color.BLUE).endVertex()
    buffer.addVertex(-0.5f, -0.5f, 0f).setColor(Color.RED).endVertex()
    buffer.addVertex(0.5f, -0.5f, 0f).setColor(Color.GREEN).endVertex()

    // Also don't forget end writing to buffer.
    buffer.end()

    // After writing vertex data we can build it to vertex buffer.
    val vertexBuffer = buffer.build()

    // For rendering you need Vertex and Fragment shaders (This assumes you have them in files/runtime, but you can view sample for shaders).

    val space = application.locationSpace // We can use classpath(), absolute() and local() functions without instance, but in this example we use instance.
    val shader = ShaderInstance.of(
        FileAsset.from(space.classpath("arc/shader/example.vsh")),
        FileAsset.from(space.classpath("arc/shader/example.fsh")),
    )
    shader.compileShaders() // Don't forget compile shaders.

    // Create simple game loop (Just as an example, you should have your own, only one game loop).
    while (!application.window.shouldClose()) {
        application.renderSystem.beginFrame() // Begin render frame.

        shader.bind() // Binds our shader in current context.

        // Now we can render our buffer via drawer.
        application.renderSystem.drawer.draw(vertexBuffer)

        shader.unbind() // In this case we don't need unbind shader, but in real games it is quite important.

        application.renderSystem.endFrame() // End render frame.
    }
}