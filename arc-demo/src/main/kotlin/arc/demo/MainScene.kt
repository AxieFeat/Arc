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
import arc.texture.TextureAtlas
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Math

class MainScene(
    private val application: Application
) : AbstractScene(application, 100f) {

    private val positionTex = VertexFormat.builder()
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.UV0)
        .build()

    private val positionTexShader = ShaderInstance.of(
        vertexShader = VertexShader.from(classpath("arc/shader/position_tex/position_tex.vsh")),
        fragmentShader = FragmentShader.from(classpath("arc/shader/position_tex/position_tex.fsh")),
        shaderData = ShaderData.from(classpath("arc/shader/position_tex/position_tex.json")),
    ).also {
        it.compileShaders()
        it.addProvider(DefaultUniformProvider)
    }

    private val atlas = TextureAtlas.from(
        asset = TextureAsset.from(
            classpath("arc/texture/cube.png"),
        ),
        rows = 2,
        columns = 2
    )

    private var cubeMatrix = Matrix4f()
    private var cubeRotation = 0f

//    private val buffer: DrawBuffer = createTexturedCubeBuffer()

    private val sensitivity = 0.1f
    private var speed = 0.02f

    init {
        camera.fov = 65f
        camera.zNear = 0.0001f

        camera.update()

        application.window.isVsync = false
    }

    override fun render() {
        if(isSkipRender) return
        updateDelta()
        camera.updateAspect(application.window.height, application.window.width)
        handleInput()

        cubeRotation += 0.001f
        cubeMatrix.rotate(
            Math.toRadians(cubeRotation), 1f, 1f, 0f
        )

        atlas.bind()
        positionTexShader.bind()

        application.renderSystem.enableDepthTest()
        drawer.draw(createTexturedCubeBuffer())
        application.renderSystem.disableDepthTest()

        atlas.unbind()
        positionTexShader.unbind()

        calculateFps()
        println(fps)
    }

    private fun handleInput() {
        val front = Vector3f(0f, 0f, -1f).rotate(camera.rotation).normalize()
        val right = Vector3f(1f, 0f, 0f).rotate(camera.rotation).normalize()
        val up = Vector3f(0f, 1f, 0f).rotate(camera.rotation).normalize()
        var newX = camera.position.x
        var newY = camera.position.y
        var newZ = camera.position.z

        speed = if(application.keyboard.isPressed(KeyCode.KEY_LCONTROL)) {
            0.005f
        } else {
            0.02f
        }

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

        if(application.mouse.isPressed(KeyCode.MOUSE_LEFT)) {
            camera.rotate(
                -application.mouse.displayVec.x * sensitivity,
                -application.mouse.displayVec.y * sensitivity,
                0f
            )
        }

        camera.update()
    }

    val texCoords = listOf(
        Pair(1, 1),
        Pair(1, 1),
        Pair(1, 1),
        Pair(1, 1),
        Pair(2, 1),
        Pair(1, 2)
    )
    val positions = floatArrayOf(
        -0.5f, -0.5f,  0.5f,   0.5f, -0.5f,  0.5f,   0.5f,  0.5f,  0.5f,   -0.5f,  0.5f,  0.5f,
        -0.5f, -0.5f, -0.5f,   0.5f, -0.5f, -0.5f,   0.5f,  0.5f, -0.5f,   -0.5f,  0.5f, -0.5f
    )
    val indices = intArrayOf(
        0, 1, 2, 2, 3, 0,  5, 4, 7, 7, 6, 5,  4, 0, 3, 3, 7, 4,
        1, 5, 6, 6, 2, 1,  3, 2, 6, 6, 7, 3,  4, 5, 1, 1, 0, 4
    )
    val uvPattern = arrayOf(
        intArrayOf(3, 2, 1, 1, 0, 3),
        intArrayOf(0, 1, 2, 2, 3, 0)
    )

    private fun createTexturedCubeBuffer(): DrawBuffer {
        val buffer = drawer.begin(DrawerMode.TRIANGLES, positionTex)

        for (i in indices.indices step 6) {
            val (row, col) = texCoords[i / 6]
            val uv = floatArrayOf(atlas.u(row - 1, col - 1), atlas.v(row - 1, col - 1),
                atlas.u(row - 1, col), atlas.v(row - 1, col - 1),
                atlas.u(row - 1, col), atlas.v(row, col - 1),
                atlas.u(row - 1, col - 1), atlas.v(row, col - 1))
            val order = uvPattern[if (i / 6 < 4) 0 else 1]

            repeat(6) { j ->
                buffer.addVertex(cubeMatrix, positions[indices[i + j] * 3], positions[indices[i + j] * 3 + 1], positions[indices[i + j] * 3 + 2])
                    .setTexture(uv[order[j] * 2], uv[order[j] * 2 + 1])
                    .endVertex()
            }
        }

        buffer.end()
        return buffer
    }
}