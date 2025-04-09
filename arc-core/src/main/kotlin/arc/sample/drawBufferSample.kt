@file:Suppress("DuplicatedCode")
package arc.sample

import arc.Application
import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.util.Color

internal fun drawBufferSample() {
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

    // Add vertex info to our buffer.
    buffer.addVertex(0f, 0.5f, 0f).setColor(Color.BLUE)
    buffer.addVertex(-0.5f, -0.5f, 0f).setColor(Color.RED)
    buffer.addVertex(0.5f, -0.5f, 0f).setColor(Color.GREEN)
}