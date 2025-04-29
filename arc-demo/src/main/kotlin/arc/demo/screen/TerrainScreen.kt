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
import arc.math.AABB
import arc.math.Vec3f
import arc.model.Face
import arc.shader.ShaderInstance
import arc.util.Color
import org.joml.Matrix4f
import org.lwjgl.opengl.ARBUniformBufferObject.GL_INVALID_INDEX
import org.lwjgl.opengl.ARBUniformBufferObject.glBindBufferBase
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL15C.glBufferSubData
import org.lwjgl.opengl.GL20.glGetUniformLocation
import org.lwjgl.opengl.GL20.glUniform1i
import org.lwjgl.opengl.GL31.*
import org.lwjgl.system.MemoryUtil.memAlloc
import java.nio.ByteBuffer
import java.util.*
import kotlin.math.*

object TerrainScreen : Screen("terrain") {

    private val world = World()
    private val player = Player(world, camera)

    private val font = FontLoader.load(classpath("arc/font/Monocraft.json").asFileAsset())
    private val matrix = Matrix4f().ortho(0f, application.window.width.toFloat(), 0f, application.window.height.toFloat(), -1f, 1f)
    private val hello = font.prepare(matrix.translate(10f, 10f, 0f).scale(5f), "temez gondon")

    private val lighting = listOf<Light>(
        Light(Color.RED, Vec3f.of(0f, 5f, -5f), 10f),
        Light(Color.GREEN, Vec3f.of(0f, 1f, 5f), 10f),
        Light(Color.BLUE, Vec3f.of(5f, 1f, 0f), 10f),
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

    init {
        isShowCursor = false
        application.keyboard.bindingProcessor.bind(CameraControlBind)
        application.keyboard.bindingProcessor.bind(ScreenshotBind)

        application.renderSystem.enableCull()
        application.renderSystem.enableDepthTest()

        application.window.isVsync = true

        player.viewDistance = 8
        player.memoryDistance = 16

        player.setPosition(0.0, 50.0, 0.0)
        player.fly = false

        for(x in 0..8) {
            for(y in 0..8) {
                world.generateChunkAt(x, y)
            }
        }

        world.allChanged()
    }

    override fun doRender() {
        player.handleInput(delta)

        ShaderContainer.blitScreen.bind()
        font.bind()
        drawer.draw(hello)

        renderCrosshair()

//        renderSky()

        ShaderContainer.positionTexColor.bind()
        uploadLights(ShaderContainer.positionTexColor, lighting)
        world.render(EmptyShaderInstance, player)
        ShaderContainer.positionTexColor.unbind()

        val aabb = generateAABBForHoveredBlock()
        if (aabb != null) {
            renderAABB(aabb)
        }
    }

    fun breakBlock() {
        val block = raycastForBlock()

        if(block != null) {
            world.setBlockAndUpdateChunk(block.x, block.y, block.z, null)
        }
    }

    fun placeBlock() {
        val hit = raycastForBlock() ?: return

        val (x, y, z, face, _) = hit

        val (nx, ny, nz) = when (face) {
            Face.UP    -> Triple(0, 1, 0)
            Face.DOWN  -> Triple(0, -1, 0)
            Face.NORTH -> Triple(0, 0, -1)
            Face.SOUTH -> Triple(0, 0, 1)
            Face.WEST  -> Triple(-1, 0, 0)
            Face.EAST  -> Triple(1, 0, 0)
        }

        val targetX = x + nx
        val targetY = y + ny
        val targetZ = z + nz

        world.setBlockAndUpdateChunk(targetX, targetY, targetZ)
    }


    override fun onFpsUpdate(fps: Int) {
        name = "FPS: $fps, Frame time: $frameTime ms"
    }

    private fun renderCrosshair() {
        ShaderContainer.blitPositionColor.bind()
        drawer.draw(crosshairBuffer)
        ShaderContainer.blitPositionColor.unbind()
    }

    private fun generateAABBForHoveredBlock(maxDistance: Float = 5f): AABB? {
        val hit = raycastForBlock(maxDistance)
        return if (hit != null) {
            val blockMin = Vec3f.of(hit.x.toFloat() - 0.001f, hit.y.toFloat() - 0.001f, hit.z.toFloat() - 0.001f)
            val blockMax = Vec3f.of((hit.x + 1.001).toFloat(), (hit.y + 1.001).toFloat(), (hit.z + 1.001).toFloat())

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
        renderStars()
    }

    private fun renderStars() {
        ShaderContainer.positionColor.bind()

        val random = Random(10842L)

        drawer.begin(DrawerMode.TRIANGLES, VertexFormatContainer.positionColor, 25000).use { buffer ->
            for (i in 0..1499) {
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

                    val vertices = Array(4) { Vec3f.of(0f, 0f, 0f) }

                    for (j in 0..3) {
                        val d18 = ((j and 2) - 1).toDouble() * d3
                        val d19 = ((j + 1 and 2) - 1).toDouble() * d3
                        val d21 = d18 * d16 - d19 * d15
                        val d22 = d19 * d16 + d18 * d15
                        val d23 = d21 * d12
                        val d24 = -d21 * d13
                        val d25 = d24 * d9 - d22 * d10
                        val d26 = d22 * d9 + d24 * d10

                        vertices[j] = Vec3f.of(
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

            buffer.build().use {
                drawer.draw(it)
            }
        }

        ShaderContainer.positionColor.unbind()
    }

    fun storeUbo(lights: List<Light>): Int {
        val maxLights = 2048
        val lightSizeBytes = 32
        val bufferSize = maxLights * lightSizeBytes

        val uboId = glGenBuffers()
        glBindBuffer(GL_UNIFORM_BUFFER, uboId)
        glBufferData(GL_UNIFORM_BUFFER, bufferSize.toLong(), GL_DYNAMIC_DRAW)

        val buffer: ByteBuffer = memAlloc(bufferSize)

        for (i in 0 until maxLights) {
            val light = lights.getOrNull(i)

            if (light != null && light.color.alpha > 0f) {
                buffer.putFloat(light.color.red / 255f)
                buffer.putFloat(light.color.green / 255f)
                buffer.putFloat(light.color.blue / 255f)
                buffer.putFloat(light.color.alpha.toFloat())

                buffer.putFloat(light.position.x)
                buffer.putFloat(light.position.y)
                buffer.putFloat(light.position.z)
                buffer.putFloat(light.radius)
            } else {
                buffer.putFloat(0f).putFloat(0f).putFloat(0f).putFloat(0f)
                buffer.putFloat(0f).putFloat(0f).putFloat(0f).putFloat(0f)
            }
        }

        buffer.flip()
        glBufferSubData(GL_UNIFORM_BUFFER, 0, buffer)

        return uboId
    }

    fun uploadLights(shader: ShaderInstance, lights: List<Light>) {
        val blockIndex = glGetUniformBlockIndex(shader.id, "Lights")
        if (blockIndex != GL_INVALID_INDEX) {
            glUniformBlockBinding(shader.id, blockIndex, 0)
            glBindBufferBase(GL_UNIFORM_BUFFER, 0, ubo)
        }

        val lightCountLocation = glGetUniformLocation(shader.id, "lightCount")
        if (lightCountLocation != -1) {
            glUniform1i(lightCountLocation, lights.size)
        }

        glBindBuffer(GL_UNIFORM_BUFFER, 0)
    }

    data class Light(
        val color: Color,
        val position: Vec3f,
        val radius: Float
    )

}