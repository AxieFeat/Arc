package arc.demo.shader

import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement

object VertexFormatContainer {

    @JvmField
    val positionTex = VertexFormat.builder()
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.UV0)
        .build()

    @JvmField
    val blitScreen = positionTex

    @JvmField
    val positionColor = VertexFormat.builder()
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.COLOR)
        .build()

}