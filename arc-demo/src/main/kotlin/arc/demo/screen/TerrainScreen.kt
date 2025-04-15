package arc.demo.screen

import arc.demo.input.CameraControlBind
import arc.demo.shader.ShaderContainer
import arc.demo.world.World
import arc.graphics.ModelHandler
import arc.input.KeyCode
import arc.math.Point2i
import arc.math.Point3d
import arc.model.Face
import arc.model.Model
import arc.model.cube.Cube
import arc.model.cube.CubeFace
import arc.model.texture.ModelTexture
import de.articdive.jnoise.core.api.functions.Interpolation
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator
import org.joml.Vector3f

object TerrainScreen : Screen("main-menu") {

    private val noise = PerlinNoiseGenerator.newBuilder().setSeed(3301).setInterpolation(Interpolation.COSINE).build();

//    private val modelHandler = ModelHandler.of(drawer, generateTerrain(128, 128))

    private val world = World().also { generateTerrain(it, 4, 4) }

    private val front = Vector3f()
    private val right = Vector3f()
    private val up = Vector3f()

    private var sensitivity = 0f
    private var speed = 0f

    init {
        camera.fov = 65f
        camera.zNear = 0.1f
        camera.zFar = 10000f
        camera.update()

        showCursor = false
        application.keyboard.bindingProcessor.bind(CameraControlBind)

        application.renderSystem.enableCull()
        application.renderSystem.enableDepthTest()

        application.window.isVsync = true
    }

    override fun doRender() {
        handleInput()

        world.render(ShaderContainer.positionTexColor)
//        val aabb = modelHandler.aabb
//
//        if(camera.frustum.isBoxInFrustum(aabb)) {
//            modelHandler.render(ShaderContainer.positionTexColor)
//        }
    }

    private fun handleInput() {
        this.front.set(0f, 0f, -1f).rotate(camera.rotation).normalize()
        this.right.set(1f, 0f, 0f).rotate(camera.rotation).normalize()
        this.up.set(0f, 1f, 0f).rotate(camera.rotation).normalize()

        var newX = camera.position.x
        var newY = camera.position.y
        var newZ = camera.position.z

        speed = if (application.keyboard.isPressed(KeyCode.KEY_LCONTROL)) {
            1f
        } else {
            5f
        } * delta

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

        if(CameraControlBind.status) {
            this.sensitivity = 65f * delta
            camera.rotate(
                -application.mouse.displayVec.y * sensitivity,
                -application.mouse.displayVec.x * sensitivity,
                0f
            )
        }

        camera.update()
        application.mouse.reset()
    }

    override fun onFpsUpdate(fps: Int) {
        name = "FPS: $fps, Frame time: $frameTime ms"
    }

    fun generateTerrain(world: World, chunkWidth: Int, chunkDepth: Int) {
        val scale = 0.1
        val maxHeight = 64

        for (chunkX in 0 until chunkWidth) {
            for (chunkZ in 0 until chunkDepth) {
                world.getOrCreateChunk(chunkX, chunkZ)

                for (localX in 0 until 16) {
                    for (localZ in 0 until 16) {
                        val worldX = chunkX * 16 + localX
                        val worldZ = chunkZ * 16 + localZ

                        val height = (perlin(worldX * scale, worldZ * scale) * 10).toInt()
                            .coerceIn(1, maxHeight)

                        for (y in 0 until height) {
                            world.setBlock(worldX, y, worldZ, model())
                        }
                    }
                }
            }
        }

        world.allChanged()
    }

    private fun model() = Model.of(
        elements = mutableListOf(
            Cube.of(
                from = Point3d.of(0.0, 0.0, 0.0),
                to = Point3d.of(1.0, 1.0, 1.0),
                faces = mapOf(
                    Face.NORTH to defaultFace(),
                    Face.SOUTH to defaultFace(),
                    Face.WEST to defaultFace(),
                    Face.EAST to defaultFace(),
                    Face.UP to defaultFace(),
                    Face.DOWN to defaultFace()
                )
            )
        ),
        textures = mutableListOf(
            ModelTexture.of(
                id = 0,
                width = 16,
                height = 16,
                base64Image = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAAAAAA6mKC9AAAAZElEQVR42jWNsQ0AMQjEGJcB3HsEr/xSkncBOgkfUyXIguoo4mEXGdkFFvBdlGmpwKCAWIWOeYFladCK4n2BXZZn6vR3dsXRfLnKWfFoXmtUrX81AiboaZ9u4I1G8KdqPICl6AdyLn2NfcJFIAAAAABJRU5ErkJggg=="
            )
        )
    )

    private fun defaultFace(): CubeFace {
        return CubeFace.of(
            uvMin = Point2i.of(0, 0),
            uvMax = Point2i.of(16, 16),
            texture = 0
        )
    }

    private fun perlin(x: Double, y: Double): Double {
        val value = noise.evaluateNoise(x, y)

        return (value + 1.0) / 2.0
    }

}