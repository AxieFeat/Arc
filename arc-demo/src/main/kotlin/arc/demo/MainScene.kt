package arc.demo

import arc.Application
import arc.assets.TextureAsset
import arc.assets.shader.FragmentShader
import arc.assets.shader.ShaderData
import arc.assets.shader.VertexShader
import arc.demo.shader.DefaultUniformProvider
import arc.files.classpath
import arc.graphics.AbstractScene
import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.input.KeyCode
import arc.shader.ShaderInstance
import arc.texture.Texture
import org.joml.Vector3f

class MainScene(
    private val application: Application
) : AbstractScene(application, 100f) {

    private val positionTex = VertexFormat.builder()
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.UV0)
        .build()


    private val positionTexShader = ShaderInstance.of(
        VertexShader.from(classpath("arc/shader/position_tex/position_tex.vsh")),
        FragmentShader.from(classpath("arc/shader/position_tex/position_tex.fsh")),
        ShaderData.from(classpath("arc/shader/position_tex/position_tex.json")),
    ).also {
        it.compileShaders()
        it.addProvider(DefaultUniformProvider)
    }

    private val texture = Texture.from(
        TextureAsset.from(
            classpath("arc/texture/cube.png"),
        )
    )

    private val buffer: DrawBuffer = createTexturedCubeBuffer()

    private val sensitivity = 0.1f
    private val speed = 0.02f

    init {
        showCursor = false

        camera.fov = 65f

        camera.update()
    }

    override fun render() {
        if(isSkipRender) return
        updateDelta()
        camera.updateAspect(application.window.height, application.window.width)
        handleInput()

//        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE)

        texture.bind()
        positionTexShader.bind()

        application.renderSystem.enableDepthTest()
        drawer.draw(buffer)
        application.renderSystem.disableDepthTest()

        texture.unbind()
        positionTexShader.unbind()

//        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL)

        calculateFps()
    }

    private fun handleInput() {
        val front = Vector3f(0f, 0f, -1f).rotate(camera.rotation).normalize()
        val right = Vector3f(1f, 0f, 0f).rotate(camera.rotation).normalize()
        val up = Vector3f(0f, 1f, 0f).rotate(camera.rotation).normalize()

        var newX = camera.position.x
        var newY = camera.position.y
        var newZ = camera.position.z

        if (application.keyboard.isPressed(KeyCode.KEY_W)) {
            newX += front.x * speed
            newY += front.y * speed
            newZ += front.z * speed
        }
        if (application.keyboard.isPressed(KeyCode.KEY_S)) {
            newX -= front.x * speed
            newY -= front.y * speed
            newZ -= front.z * speed
        }
        if (application.keyboard.isPressed(KeyCode.KEY_A)) {
            newX -= right.x * speed
            newY -= right.y * speed
            newZ -= right.z * speed
        }
        if (application.keyboard.isPressed(KeyCode.KEY_D)) {
            newX += right.x * speed
            newY += right.y * speed
            newZ += right.z * speed
        }
        if(application.keyboard.isPressed(KeyCode.KEY_SPACE)) {
            newX += up.x * speed
            newY += up.y * speed
            newZ += up.z * speed
        }
        if(application.keyboard.isPressed(KeyCode.KEY_LSHIFT)) {
            newX -= up.x * speed
            newY -= up.y * speed
            newZ -= up.z * speed
        }

        camera.position.x = newX
        camera.position.y = newY
        camera.position.z = newZ

        camera.rotate(
            -application.mouse.displayVec.x * sensitivity,
            -application.mouse.displayVec.y * sensitivity,
            0f
        )

        camera.update()
    }

    private fun createTexturedCubeBuffer(): DrawBuffer {
        val buffer = drawer.begin(DrawerMode.TRIANGLES, positionTex)

        // UV-координаты для каждой грани
        val uvSide = floatArrayOf(0.5f, 0.5f,  0f, 0.5f,  0f, 0f,  0.5f, 0f) // Боковая (левая верхняя часть атласа)
        val uvBottom = floatArrayOf(0.5f, 0f,  1f, 0f,  1f, 0.5f,  0.5f, 0.5f) // Нижняя (правая верхняя часть атласа)
        val uvTop = floatArrayOf(0f, 0.5f,  0.5f, 0.5f,  0.5f, 1f,  0f, 1f) // Верхняя (левая нижняя часть атласа)

        val positions = floatArrayOf(
            // Передняя грань
            -0.5f, -0.5f,  0.5f,   // 0
            0.5f, -0.5f,  0.5f,   // 1
            0.5f,  0.5f,  0.5f,   // 2
            -0.5f,  0.5f,  0.5f,   // 3

            // Задняя грань
            -0.5f, -0.5f, -0.5f,   // 4
            0.5f, -0.5f, -0.5f,   // 5
            0.5f,  0.5f, -0.5f,   // 6
            -0.5f,  0.5f, -0.5f    // 7
        )

        val indices = intArrayOf(
            // Передняя грань
            0, 1, 2, 2, 3, 0,
            // Задняя грань
            5, 4, 7, 7, 6, 5,
            // Левая грань
            4, 0, 3, 3, 7, 4,
            // Правая грань
            1, 5, 6, 6, 2, 1,
            // Верхняя грань
            3, 2, 6, 6, 7, 3,
            // Нижняя грань
            4, 5, 1, 1, 0, 4
        )

        // UV-координаты для каждой грани
        val uvList = listOf(
            uvSide,   // Передняя
            uvSide,   // Задняя
            uvSide,   // Левая
            uvSide,   // Правая
            uvTop,    // Верхняя
            uvBottom  // Нижняя
        )

        // Порядок UV-координат для каждой грани
        val uvOrder = listOf(
            intArrayOf(0, 1, 2, 2, 3, 0), // Передняя
            intArrayOf(1, 0, 3, 3, 2, 1), // Задняя (перевернута по горизонтали)
            intArrayOf(1, 0, 3, 3, 2, 1), // Левая (перевернута по горизонтали)
            intArrayOf(0, 1, 2, 2, 3, 0), // Правая
            intArrayOf(3, 2, 1, 1, 0, 3), // Верхняя (перевернута по вертикали)
            intArrayOf(0, 1, 2, 2, 3, 0)  // Нижняя
        )

        for (i in indices.indices step 6) {
            val faceIndex = i / 6
            val uv = uvList[faceIndex]
            val order = uvOrder[faceIndex]

            for (j in 0 until 6) {
                val index = indices[i + j]
                val uvIndex = order[j]
                val u = uv[uvIndex * 2]
                val v = uv[uvIndex * 2 + 1]

                buffer
                    .addVertex(positions[index * 3], positions[index * 3 + 1], positions[index * 3 + 2])
                    .setTexture(u, v)
                    .endVertex()
            }
        }

        buffer.end()
        return buffer
    }
}