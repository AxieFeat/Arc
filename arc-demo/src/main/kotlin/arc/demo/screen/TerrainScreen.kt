package arc.demo.screen

import arc.asset.asFileAsset
import arc.demo.entity.Player
import arc.demo.font.FontLoader
import arc.demo.input.CameraControlBind
import arc.demo.input.ScreenshotBind
import arc.demo.shader.ShaderContainer
import arc.demo.shader.VertexFormatContainer
import arc.demo.world.World
import arc.demo.world.block.Block
import arc.files.classpath
import arc.graphics.DrawerMode
import arc.graphics.EmptyShaderInstance
import arc.input.bind
import arc.input.keyboard
import arc.math.AABB
import arc.model.Face
import arc.shader.ShaderInstance
import arc.shader.UniformBuffer
import arc.util.Color
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.component1
import org.joml.component2
import org.joml.component3
import java.lang.Math
import java.util.*
import kotlin.math.*

object TerrainScreen : Screen("terrain") {

    private val drawer = application.renderSystem.drawer

    private val world = World()
    private val player = Player(world, camera)

    private val font = FontLoader.load(classpath("arc/font/Monocraft.json").asFileAsset())
    private val matrix = Matrix4f().ortho(0f, application.window.width.toFloat(), 0f, application.window.height.toFloat(), -1f, 1f)
    private val hello = font.prepare(matrix.translate(10f, 10f, 0f).scale(5f), "temez gondon")

    private val lighting = listOf<Light>(
        Light(Color.RED, Vector3f(0f, 5f, -5f), 10f),
        Light(Color.GREEN, Vector3f(0f, 1f, 5f), 10f),
        Light(Color.BLUE, Vector3f(5f, 1f, 0f), 10f),
    )
    private val ubo = storeUbo(lighting)

    val crosshairBuffer = drawer.begin(DrawerMode.LINES, VertexFormatContainer.positionColor).use { buffer ->
        val crossSize = 1f / 20f
        buffer.addVertex(0f - crossSize, 0f, 0f).setColor(Color.RED)
        buffer.addVertex(0f + crossSize, 0f, 0f).setColor(Color.RED)

        buffer.addVertex(0f, 0f - crossSize, 0f).setColor(Color.RED)
        buffer.addVertex(0f, 0f + crossSize, 0f).setColor(Color.RED)

        buffer.build()
    }

    // Stars from minecraft 1.8
    val starsBuffer = drawer.begin(DrawerMode.TRIANGLES, VertexFormatContainer.positionColor, 19000).use { buffer ->
        val random = Random(10842L)

        repeat(1499) {
            var d0 = (random.nextFloat() * 2.0f - 1.0f).toDouble()
            var d1 = (random.nextFloat() * 2.0f - 1.0f).toDouble()
            var d2 = (random.nextFloat() * 2.0f - 1.0f).toDouble()
            val d3 = (0.15f + random.nextFloat() * 0.1f).toDouble()
            var d4 = d0 * d0 + d1 * d1 + d2 * d2

            if (d4 < 1.0 && d4 > 0.01) {
                d4 = 1.0 / sqrt(d4)
                d0 *= d4
                d1 *= d4
                d2 *= d4

                val d5 = d0 * 100.0
                val d6 = d1 * 100.0
                val d7 = d2 * 100.0
                val d8 = atan2(d0, d2)
                val d9 = sin(d8)
                val d10 = cos(d8)
                val d11 = atan2(sqrt(d0 * d0 + d2 * d2), d1)
                val d12 = sin(d11)
                val d13 = cos(d11)
                val d14 = random.nextDouble() * Math.PI * 2.0
                val d15 = sin(d14)
                val d16 = cos(d14)

                val vertices = Array(4) { Vector3f(0f, 0f, 0f) }

                for (j in 0..3) {
                    val d18 = ((j and 2) - 1).toDouble() * d3
                    val d19 = ((j + 1 and 2) - 1).toDouble() * d3
                    val d21 = d18 * d16 - d19 * d15
                    val d22 = d19 * d16 + d18 * d15
                    val d23 = d21 * d12
                    val d24 = -d21 * d13
                    val d25 = d24 * d9 - d22 * d10
                    val d26 = d22 * d9 + d24 * d10

                    vertices[j] = Vector3f(
                        (d5 + d25).toFloat(),
                        (d6 + d23).toFloat(),
                        (d7 + d26).toFloat()
                    )
                }

                buffer.addVertex(vertices[0].x, vertices[0].y, vertices[0].z).setColor(Color.WHITE)
                buffer.addVertex(vertices[1].x, vertices[1].y, vertices[1].z).setColor(Color.WHITE)
                buffer.addVertex(vertices[2].x, vertices[2].y, vertices[2].z).setColor(Color.WHITE)

                buffer.addVertex(vertices[0].x, vertices[0].y, vertices[0].z).setColor(Color.WHITE)
                buffer.addVertex(vertices[2].x, vertices[2].y, vertices[2].z).setColor(Color.WHITE)
                buffer.addVertex(vertices[3].x, vertices[3].y, vertices[3].z).setColor(Color.WHITE)
            }
        }

        buffer.build()
    }

    init {
        isShowCursor = false
        application.keyboard.bind(CameraControlBind)
        application.keyboard.bind(ScreenshotBind)

        application.renderSystem.enableCull()

        application.window.isVsync = true

        player.viewDistance = 8
        player.memoryDistance = 16

        player.position = Vector3f(0f, 50f, 0f)
        player.fly = false

//        for(x in 0..8) {
//            for(y in 0..8) {
//                world.generateChunkAt(x, y)
//            }
//        }

//        world.allChanged()
    }

    override fun doRender() {
        player.handleInput(delta)

        renderSky()

        ShaderContainer.blitScreen.bind()
        font.bind()
        drawer.draw(hello)

        renderCrosshair()

        ShaderContainer.positionTexColor.bind()
        uploadLights(ShaderContainer.positionTexColor, lighting, ubo)
        world.render(EmptyShaderInstance, player)
        ShaderContainer.positionTexColor.unbind()

        val aabb = generateAABBForHoveredBlock()
        if (aabb != null) {
            renderAABB(aabb)
        }

        name = "FPS: $fps, Frame time: $frameTime ms"
    }

    fun breakBlock() {
        val block = raycastForBlock()

        if(block != null) {
            world.setBlockAndUpdateChunk(block.x, block.y, block.z, null)
        }
    }

    fun placeBlock() {
        val hit = raycastForBlock() ?: return

        val (x, y, z) = hit

        val (nx, ny, nz) = Face.UP.normal

        val targetX = x + nx
        val targetY = y + ny
        val targetZ = z + nz

        world.setBlockAndUpdateChunk(targetX.toInt(), targetY.toInt(), targetZ.toInt())
    }

    private fun renderCrosshair() {
        ShaderContainer.blitPositionColor.bind()
        drawer.draw(crosshairBuffer)
        ShaderContainer.blitPositionColor.unbind()
    }

    private fun generateAABBForHoveredBlock(maxDistance: Float = 5f): AABB? {
        val hit = raycastForBlock(maxDistance)
        return if (hit != null) {
            val blockMin = Vector3f(hit.x.toFloat() - 0.001f, hit.y.toFloat() - 0.001f, hit.z.toFloat() - 0.001f)
            val blockMax = Vector3f((hit.x + 1.001).toFloat(), (hit.y + 1.001).toFloat(), (hit.z + 1.001).toFloat())

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

    data class RaycastHit(
        val x: Int,
        val y: Int,
        val z: Int,
        val face: Face,
        val block: Block
    )

    private fun raycastForBlock(maxDistance: Float = 6f): RaycastHit? {
        val origin = camera.ray.origin
        val direction = camera.ray.direction

        var x = origin.x.toInt()
        var y = origin.y.toInt()
        var z = origin.z.toInt()

        val stepX = if (direction.x < 0) -1 else 1
        val stepY = if (direction.y < 0) -1 else 1
        val stepZ = if (direction.z < 0) -1 else 1

        val tDeltaX = if (direction.x != 0f) abs(1f / direction.x) else Float.POSITIVE_INFINITY
        val tDeltaY = if (direction.y != 0f) abs(1f / direction.y) else Float.POSITIVE_INFINITY
        val tDeltaZ = if (direction.z != 0f) abs(1f / direction.z) else Float.POSITIVE_INFINITY

        val blockX = floor(origin.x).toInt()
        val blockY = floor(origin.y).toInt()
        val blockZ = floor(origin.z).toInt()

        var tMaxX = if (direction.x < 0) (origin.x - blockX) * tDeltaX else (blockX + 1 - origin.x) * tDeltaX
        var tMaxY = if (direction.y < 0) (origin.y - blockY) * tDeltaY else (blockY + 1 - origin.y) * tDeltaY
        var tMaxZ = if (direction.z < 0) (origin.z - blockZ) * tDeltaZ else (blockZ + 1 - origin.z) * tDeltaZ

        var distanceTraveled = 0f

        var lastStepFace: Face = Face.UP

        while (distanceTraveled <= maxDistance) {
            val block = world.getBlock(x, y, z)
            if (block != null) {
                return RaycastHit(x, y, z, lastStepFace, block)
            }

            if (tMaxX < tMaxY && tMaxX < tMaxZ) {
                x += stepX
                distanceTraveled = tMaxX
                tMaxX += tDeltaX
                lastStepFace = if (stepX > 0) Face.WEST else Face.EAST
            } else if (tMaxY < tMaxZ) {
                y += stepY
                distanceTraveled = tMaxY
                tMaxY += tDeltaY
                lastStepFace = if (stepY > 0) Face.DOWN else Face.UP
            } else {
                z += stepZ
                distanceTraveled = tMaxZ
                tMaxZ += tDeltaZ
                lastStepFace = if (stepZ > 0) Face.NORTH else Face.SOUTH
            }
        }

        return null
    }

    private fun renderSky() {
        application.renderSystem.disableDepthTest()
        renderStars()
        application.renderSystem.enableDepthTest()
    }

    private fun renderStars() {
        ShaderContainer.sky.bind()

        drawer.draw(starsBuffer)

        ShaderContainer.sky.unbind()
    }

    fun storeUbo(lights: List<Light>): UniformBuffer {
        val maxLights = 2048
        val lightSizeBytes = 32
        val bufferSize = maxLights * lightSizeBytes

        val ubo = UniformBuffer.of(bufferSize)

        ubo.update {
            for (i in 0 until maxLights) {
                val light = lights.getOrNull(i)

                if (light != null && light.color.alpha > 0f) {
                    this.putFloat(light.color.red / 255f)
                    this.putFloat(light.color.green / 255f)
                    this.putFloat(light.color.blue / 255f)
                    this.putFloat(light.color.alpha.toFloat())

                    this.putFloat(light.position.x)
                    this.putFloat(light.position.y)
                    this.putFloat(light.position.z)
                    this.putFloat(light.radius)
                } else {
                    this.putFloat(0f).putFloat(0f).putFloat(0f).putFloat(0f)
                    this.putFloat(0f).putFloat(0f).putFloat(0f).putFloat(0f)
                }
            }
        }

        return ubo
    }

    fun uploadLights(shader: ShaderInstance, lights: List<Light>, uniformBuffer: UniformBuffer) {
        shader.uploadUniformBuffer("Lights", uniformBuffer)
        shader.setUniform("lightCount", lights.size)
    }

    data class Light(
        val color: Color,
        val position: Vector3f,
        val radius: Float
    )

}