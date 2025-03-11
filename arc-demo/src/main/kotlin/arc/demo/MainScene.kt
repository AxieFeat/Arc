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
import arc.math.Point3d
import arc.shader.ShaderInstance

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

    private val buffer: DrawBuffer = createCubeBuffer()

    private var rotationAngle = 0f

    init {
        camera.fov = 40f
        camera.position = Point3d.of(
            1.5, 2.5, 2.0,
        )
        camera.lookAt(Point3d.ZERO)

        camera.update()
    }

    override fun render() {
        if(isSkipRender) return
        updateDelta()
        camera.updateAspect(application.window.height, application.window.width)

        rotationAngle += delta * 0.001f
        camera.rotate(-rotationAngle, rotationAngle, -rotationAngle)
        camera.update()

//        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE)

        shader.bind()

        application.renderSystem.enableDepthTest()
        drawer.draw(buffer)
        application.renderSystem.disableDepthTest()

        shader.unbind()

//        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL)

        calculateFps()
    }

    private fun createCubeBuffer(): DrawBuffer {
        val buffer = drawer.begin(DrawerMode.TRIANGLES, positionColor)

        val positions = floatArrayOf(
            -0.5f, -0.5f,  0.5f,  // 0
            0.5f, -0.5f,  0.5f,  // 1
            0.5f,  0.5f,  0.5f,  // 2
            -0.5f,  0.5f,  0.5f,  // 3

            -0.5f, -0.5f, -0.5f,  // 4
            0.5f, -0.5f, -0.5f,  // 5
            0.5f,  0.5f, -0.5f,  // 6
            -0.5f,  0.5f, -0.5f   // 7
        )

        val colors = arrayOf(
            intArrayOf(255, 0, 0, 255),
            intArrayOf(0, 255, 0, 255),
            intArrayOf(0, 0, 255, 255),
            intArrayOf(255, 255, 0, 255),
            intArrayOf(255, 0, 255, 255),
            intArrayOf(0, 255, 255, 255)
        )

        val indices = intArrayOf(
            0, 1, 2, 2, 3, 0,
            5, 4, 7, 7, 6, 5,
            4, 0, 3, 3, 7, 4,
            1, 5, 6, 6, 2, 1,
            3, 2, 6, 6, 7, 3,
            4, 5, 1, 1, 0, 4
        )

        for (i in indices.indices step 6) {
            val color = colors[i / 6]
            for (j in 0 until 6) {
                val index = indices[i + j]
                buffer.addVertex(positions[index * 3], positions[index * 3 + 1], positions[index * 3 + 2])
                    .setColor(color[0], color[1], color[2], color[3])
            }
        }

        buffer.end()
        return buffer
    }
}