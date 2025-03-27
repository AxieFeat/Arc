package arc.demo.screen

import arc.assets.TextureAsset
import arc.demo.entity.CubeEntity
import arc.demo.shader.ShaderContainer
import arc.demo.shader.VertexFormatContainer
import arc.files.classpath
import arc.input.KeyCode
import arc.texture.TextureAtlas
import org.joml.Vector3f

object MainMenuScreen : Screen("main-menu") {

    private val grassAtlas = TextureAtlas.from(
        asset = TextureAsset.from(
            classpath("arc/texture/grass.png"),
        ),
        rows = 2,
        columns = 2
    )

    private val cube = CubeEntity(application, grassAtlas, VertexFormatContainer.positionTex)

    private val front = Vector3f()
    private val right = Vector3f()
    private val up = Vector3f()

    private var sensitivity = 0f
    private var speed = 0f

    init {
        camera.fov = 65f
        camera.zNear = 0.0001f
        camera.zFar = 10000000000000000000000000000000000f
        camera.update()

        application.renderSystem.enableCull()
    }

    override fun doRender() {
        handleInput()

        cube.render(ShaderContainer.positionTex)
    }

    private fun handleInput() {
        this.front.set(0f, 0f, -1f).rotate(camera.rotation).normalize()
        this.right.set(1f, 0f, 0f).rotate(camera.rotation).normalize()
        this.up.set(0f, 1f, 0f).rotate(camera.rotation).normalize()

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
            this.sensitivity = 65f * delta
            camera.rotate(
                -application.mouse.displayVec.x * sensitivity,
                -application.mouse.displayVec.y * sensitivity,
                0f
            )
        }

        camera.update()
    }

    override fun onFpsUpdate(fps: Int) {
        name = "FPS: $fps"
    }

}