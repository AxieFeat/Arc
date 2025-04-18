package arc.demo.screen

import arc.demo.input.CameraControlBind
import arc.demo.shader.ShaderContainer
import arc.demo.shader.VertexFormatContainer
import arc.demo.world.World
import arc.demo.world.block.Block
import arc.graphics.DrawerMode
import arc.input.KeyCode
import arc.math.AABB
import arc.math.Vec3f
import arc.model.Face
import arc.model.Model
import arc.model.cube.Cube
import arc.model.cube.CubeFace
import arc.model.texture.ModelTexture
import arc.util.Color
import de.articdive.jnoise.core.api.functions.Interpolation
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator
import org.joml.Vector3f
import org.lwjgl.opengl.GL31
import kotlin.math.abs

object TerrainScreen : Screen("main-menu") {

    private val noise = PerlinNoiseGenerator.newBuilder().setSeed(3301).setInterpolation(Interpolation.COSINE).build();

    private val world = World().also { generateTerrain(it, 2, 2) }

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

        isShowCursor = false
        application.keyboard.bindingProcessor.bind(CameraControlBind)
//        application.mouse.bindingProcessor.bind(BreakBlockInput)

        application.renderSystem.enableCull()
        application.renderSystem.enableDepthTest()

        application.window.isVsync = true
    }

    override fun doRender() {
        handleInput()

        renderCrosshair(0f, 0f, 1f)

        world.render(ShaderContainer.positionTexColor)

        val aabb = generateAABBForHoveredBlock()
        if (aabb != null) {
            GL31.glLineWidth(2.0f)
            renderAABB(aabb)
        }
    }

    fun breakBlock() {
        val block = raycastForBlock()

        if(block != null) {
            world.setBlockAndUpdateChunk(block.x, block.y, block.z, null)
        }
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
        if(application.mouse.isPressed(KeyCode.MOUSE_LEFT)) {
            breakBlock()
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
        println("World generated!")
    }

    private fun model() = Model.builder()
        .addCube(
            Cube.builder()
                .setFrom(0f, 0f, 0f)
                .setTo(1f, 1f, 1f)
                .addFace(Face.NORTH, defaultFace())
                .addFace(Face.SOUTH, defaultFace())
                .addFace(Face.WEST, defaultFace())
                .addFace(Face.EAST, defaultFace())
                .addFace(Face.UP, defaultFace())
                .addFace(Face.DOWN, defaultFace())
                .build()
        )
        .setTexture(
            ModelTexture.builder()
                .setImage("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAAAAAA6mKC9AAAAZElEQVR42jWNsQ0AMQjEGJcB3HsEr/xSkncBOgkfUyXIguoo4mEXGdkFFvBdlGmpwKCAWIWOeYFladCK4n2BXZZn6vR3dsXRfLnKWfFoXmtUrX81AiboaZ9u4I1G8KdqPICl6AdyLn2NfcJFIAAAAABJRU5ErkJggg==")
                .build()
        )
        .build()

    private fun defaultFace(): CubeFace {
        return CubeFace.builder()
            .setUvMin(0, 0)
            .setUvMax(16, 16)
            .build()
    }

    private fun perlin(x: Double, y: Double): Double {
        val value = noise.evaluateNoise(x, y)

        return (value + 1.0) / 2.0
    }

    private fun renderCrosshair(centerX: Float, centerY: Float, size: Float) {
        ShaderContainer.blitPositionColor.bind()
        val crossSize = size / 20
        drawer.begin(DrawerMode.LINES, VertexFormatContainer.positionColor).use { buffer ->
            buffer.addVertex(centerX - crossSize, centerY, 0f).setColor(Color.RED)
            buffer.addVertex(centerX + crossSize, centerY, 0f).setColor(Color.RED)

            buffer.addVertex(centerX, centerY - crossSize, 0f).setColor(Color.RED)
            buffer.addVertex(centerX, centerY + crossSize, 0f).setColor(Color.RED)

            buffer.build().use {
                drawer.draw(it)
            }
        }
        ShaderContainer.blitPositionColor.unbind()
    }

    private fun generateAABBForHoveredBlock(maxDistance: Float = 5f): AABB? {
        val block = raycastForBlock(maxDistance)
        return if (block != null) {
            val blockMin = Vec3f.of(block.x.toFloat(), block.y.toFloat(), block.z.toFloat())
            val blockMax = Vec3f.of((block.x + 1).toFloat(), (block.y + 1).toFloat(), (block.z + 1).toFloat())

            AABB.of(blockMin, blockMax)
        } else {
            null
        }
    }

    private fun renderAABB(aabb: AABB) {
        ShaderContainer.positionColor.bind()
        drawer.begin(DrawerMode.LINES, VertexFormatContainer.positionColor).use { buffer ->
            val (x0, y0, z0) = aabb.min
            val (x1, y1, z1) = aabb.max
            val color = Color.BLUE

            buffer.addVertex(x0, y0, z0).setColor(color)
            buffer.addVertex(x1, y0, z0).setColor(color)

            buffer.addVertex(x1, y0, z0).setColor(color)
            buffer.addVertex(x1, y0, z1).setColor(color)

            buffer.addVertex(x1, y0, z1).setColor(color)
            buffer.addVertex(x0, y0, z1).setColor(color)

            buffer.addVertex(x0, y0, z1).setColor(color)
            buffer.addVertex(x0, y0, z0).setColor(color)


            buffer.addVertex(x0, y1, z0).setColor(color)
            buffer.addVertex(x1, y1, z0).setColor(color)

            buffer.addVertex(x1, y1, z0).setColor(color)
            buffer.addVertex(x1, y1, z1).setColor(color)

            buffer.addVertex(x1, y1, z1).setColor(color)
            buffer.addVertex(x0, y1, z1).setColor(color)

            buffer.addVertex(x0, y1, z1).setColor(color)
            buffer.addVertex(x0, y1, z0).setColor(color)


            buffer.addVertex(x0, y0, z0).setColor(color)
            buffer.addVertex(x0, y1, z0).setColor(color)

            buffer.addVertex(x1, y0, z0).setColor(color)
            buffer.addVertex(x1, y1, z0).setColor(color)

            buffer.addVertex(x1, y0, z1).setColor(color)
            buffer.addVertex(x1, y1, z1).setColor(color)

            buffer.addVertex(x0, y0, z1).setColor(color)
            buffer.addVertex(x0, y1, z1).setColor(color)

            buffer.build().use {
                drawer.draw(it)
            }
        }
        ShaderContainer.positionColor.unbind()
    }


    private fun raycastForBlock(maxDistance: Float = 5f): Block? {
        val rayOrigin = camera.ray.origin
        val rayDirection = camera.ray.direction

        val dirX = rayDirection.x
        val dirY = rayDirection.y
        val dirZ = rayDirection.z

        var x = rayOrigin.x.toInt()
        var y = rayOrigin.y.toInt()
        var z = rayOrigin.z.toInt()

        val stepX = if (dirX > 0) 1 else -1
        val stepY = if (dirY > 0) 1 else -1
        val stepZ = if (dirZ > 0) 1 else -1

        val tDeltaX = if (dirX != 0f) 1f / abs(dirX) else Float.MAX_VALUE
        val tDeltaY = if (dirY != 0f) 1f / abs(dirY) else Float.MAX_VALUE
        val tDeltaZ = if (dirZ != 0f) 1f / abs(dirZ) else Float.MAX_VALUE

        var tMaxX = if (dirX > 0) (x + 1 - rayOrigin.x.toInt()) * tDeltaX else (x - rayOrigin.x.toInt()) * tDeltaX
        var tMaxY = if (dirY > 0) (y + 1 - rayOrigin.y.toInt()) * tDeltaY else (y - rayOrigin.y.toInt()) * tDeltaY
        var tMaxZ = if (dirZ > 0) (z + 1 - rayOrigin.z.toInt()) * tDeltaZ else (z - rayOrigin.z.toInt()) * tDeltaZ

        var distance = 0f

        while (distance < maxDistance) {
            val block = world.getBlock(x, y, z)
            if (block != null) {
                return block
            }

            if (tMaxX < tMaxY) {
                if (tMaxX < tMaxZ) {
                    x += stepX
                    distance = tMaxX
                    tMaxX += tDeltaX
                } else {
                    z += stepZ
                    distance = tMaxZ
                    tMaxZ += tDeltaZ
                }
            } else {
                if (tMaxY < tMaxZ) {
                    y += stepY
                    distance = tMaxY
                    tMaxY += tDeltaY
                } else {
                    z += stepZ
                    distance = tMaxZ
                    tMaxZ += tDeltaZ
                }
            }
        }

        return null
    }


}