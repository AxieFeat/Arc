package arc.demo

import arc.Application
import arc.assets.TextureAsset
import arc.assets.shader.FragmentShader
import arc.assets.shader.ShaderData
import arc.assets.shader.VertexShader
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
import org.lwjgl.opengl.GL11

class MainScene(
    private val application: Application
) : AbstractScene(application, 100f) {

    private val positionColor = VertexFormat.builder() // Configure vertex format.
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.COLOR)
        .build()

    private val positionTex = VertexFormat.builder()
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.UV0)
        .build()

    private val shader = ShaderInstance.of(
        VertexShader.from(classpath("arc/shader/position_color/position_color.vsh")),
        FragmentShader.from(classpath("arc/shader/position_color/position_color.fsh")),
        ShaderData.from(classpath("arc/shader/position_color/position_color.json")),
    ).also { it.compileShaders() }

    private val texture = Texture.from(
        TextureAsset.from(
            classpath("arc/texture/cube.png"),
        )
    )

    private val buffer: DrawBuffer = createCubeBuffer()

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

        shader.bind()
        texture.bind()

        application.renderSystem.enableDepthTest()
        drawer.draw(buffer)
        application.renderSystem.disableDepthTest()

        texture.unbind()
        shader.unbind()

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
                buffer
                    .addVertex(positions[index * 3], positions[index * 3 + 1], positions[index * 3 + 2])
                    .setColor(color[0], color[1], color[2], color[3])
//                    .setTexture(0, 0)
            }
        }

        buffer.end()
        return buffer
    }
}