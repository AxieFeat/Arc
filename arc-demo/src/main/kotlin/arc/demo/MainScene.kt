package arc.demo

import arc.Application
import arc.assets.shader.FragmentShader
import arc.assets.shader.ShaderData
import arc.assets.shader.VertexShader
import arc.files.classpath
import arc.graphics.AbstractScene
import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.shader.ShaderInstance
import arc.util.Color

class MainScene(
    private val application: Application
) : AbstractScene(application, 100f) {

    private val positionColor = VertexFormat.builder() // Configure vertex format.
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.COLOR)
        .build()

    private val shader = ShaderInstance.of(
        VertexShader.from(classpath("arc/shader/position_color/position_color.vsh")),
        FragmentShader.from(classpath("arc/shader/position_color/position_color.fsh")),
        ShaderData.from(classpath("arc/shader/position_color/position_color.json")),
    ).also { it.compileShaders() }

    private val buffer: DrawBuffer = createBuffer()

    override fun render() {
        if(isSkipRender) return
        updateDelta()

        shader.bind()
        application.renderSystem.drawer.draw(buffer)
        shader.unbind()

        calculateFps()
    }

    private fun createBuffer(): DrawBuffer {
        val buffer = application.renderSystem.drawer.begin(
            DrawerMode.TRIANGLES,
            positionColor
        )

        // Write values to buffer.
        buffer.addVertex(0.5f, 0.5f, 0f).setColor(Color.GREEN)
        buffer.addVertex(-0.5f, 0.5f, 0f).setColor(Color.RED)
        buffer.addVertex(0f, -0.5f, 0f).setColor(Color.YELLOW)
        buffer.end() // End writing in buffer.

        return buffer
    }

}