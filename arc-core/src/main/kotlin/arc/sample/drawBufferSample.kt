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

    // addVertex() represents VertexFormatElement.POSITION
    // setColor() represents VertexFormatElement.COLOR
    // In this way we follow the vertex format.
    // Note that the order must match the one specified in the VertexFormat.
}
