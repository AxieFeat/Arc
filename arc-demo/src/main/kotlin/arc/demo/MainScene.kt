package arc.demo

import arc.Application
import arc.assets.TextureAsset
import arc.assets.shader.FragmentShader
import arc.assets.shader.ShaderData
import arc.assets.shader.VertexShader
import arc.demo.bind.CubeRotationBind
import arc.demo.cube.CubeEntity
import arc.demo.shader.DefaultUniformProvider
import arc.files.classpath
import arc.graphics.AbstractScene
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.input.KeyCode
import arc.shader.ShaderInstance
import arc.texture.TextureAtlas
import org.joml.Vector3f

class MainScene(
    private val application: Application
) : AbstractScene(application) {

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
    ).also {
        it.bind()
    }

    private val cube = CubeEntity(application, atlas, positionTex)

    private var cubeRotation = 0f
    private var rotateCubeBind = CubeRotationBind()

    private var sensitivity = 0f
    private var speed = 0f

    init {
        camera.fov = 65f
        camera.zNear = 0.0001f
        camera.update()

        application.window.isVsync = true
        application.keyboard.bindingProcessor.bind(rotateCubeBind)
    }

    override fun render() {
        if (isSkipRender) return
        updateDelta()
        camera.updateAspect(application.window.height, application.window.width)
        handleInput()

        rotateCube()
        cube.render(positionTexShader)

        calculateFps()
    }

    private fun handleInput() {
        val front = Vector3f(0f, 0f, -1f).rotate(camera.rotation).normalize()
        val right = Vector3f(1f, 0f, 0f).rotate(camera.rotation).normalize()
        val up = Vector3f(0f, 1f, 0f).rotate(camera.rotation).normalize()
        var newX = camera.position.x
        var newY = camera.position.y
        var newZ = camera.position.z

        speed = if (application.keyboard.isPressed(KeyCode.KEY_LCONTROL)) {
            1f * delta
        } else {
            5f * delta
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
        if (application.keyboard.isPressed(KeyCode.KEY_SPACE)) {
            newX += up.x * speed
            newY += up.y * speed
            newZ += up.z * speed
        }
        if (application.keyboard.isPressed(KeyCode.KEY_LSHIFT)) {
            newX -= up.x * speed
            newY -= up.y * speed
            newZ -= up.z * speed
        }

        camera.position.x = newX
        camera.position.y = newY
        camera.position.z = newZ

        if (application.mouse.isPressed(KeyCode.MOUSE_LEFT)) {
            this.sensitivity = 10f * delta
            camera.rotate(
                -application.mouse.displayVec.x * sensitivity,
                -application.mouse.displayVec.y * sensitivity,
                0f
            )
        }

        camera.update()
    }

    private fun rotateCube() {
        if(!rotateCubeBind.state) return

        cubeRotation += (120f * delta)
        cube.rotate(cubeRotation, cubeRotation, 0f)
    }
}