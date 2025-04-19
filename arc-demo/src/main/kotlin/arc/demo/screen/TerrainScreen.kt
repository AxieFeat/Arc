package arc.demo.screen

import arc.demo.entity.Player
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
import arc.util.Color
import org.joml.Vector3f
import org.lwjgl.opengl.GL31
import java.util.*
import kotlin.math.*

object TerrainScreen : Screen("terrain") {

    private val world = World()
    private val player = Player(world)
    private const val worldSize = 8

    private val front = Vector3f()
    private val right = Vector3f()
    private val up = Vector3f()

    private var sensitivity = 0f
    private var speed = 0f

    private const val fovModifier = 1.1f
    private var targetFov = 65f
    private var currentFov = 65f
    private const val fovLerpSpeed = 10f

    private var breakCooldown = 0f
    private const val breakInterval = 0.1f

    private var placeCooldown = 0f
    private const val placeInterval = 0.1f

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

        for(x in 0..<worldSize) {
            for(z in 0..<worldSize) {
                world.generateChunkAt(x, z)
            }
        }

        world.allChanged()
    }

    override fun doRender() {
        handleInput()

        renderCrosshair(0f, 0f, 1f)

        renderSky()

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


    private fun handleInput() {
        this.front.set(0f, 0f, -1f).rotate(camera.rotation).normalize()
        this.right.set(1f, 0f, 0f).rotate(camera.rotation).normalize()
        this.up.set(0f, 1f, 0f).rotate(camera.rotation).normalize()

        var newX = camera.position.x
        var newY = camera.position.y
        var newZ = camera.position.z

        speed = if (application.keyboard.isPressed(KeyCode.KEY_LCONTROL)) {
            15f
        } else {
            5f
        } * delta

        targetFov = if (application.keyboard.isPressed(KeyCode.KEY_LCONTROL)) {
            65f * fovModifier
        } else {
            65f
        }

        currentFov += (targetFov - currentFov) * min(1f, fovLerpSpeed * delta)
        camera.fov = currentFov

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

        if (application.mouse.isPressed(KeyCode.MOUSE_LEFT)) {
            breakCooldown -= delta
            if (breakCooldown <= 0f) {
                breakBlock()
                breakCooldown = breakInterval
            }
        } else {
            breakCooldown = 0f
        }

        if (application.mouse.isPressed(KeyCode.MOUSE_RIGHT)) {
            placeCooldown -= delta
            if (placeCooldown <= 0f) {
                placeBlock()
                placeCooldown = placeInterval
            }
        } else {
            placeCooldown = 0f
        }

        camera.position.x = newX
        camera.position.y = newY
        camera.position.z = newZ
        player.setPosition(newX.toFloat(), newY.toFloat(), newZ.toFloat())

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
        val hit = raycastForBlock(maxDistance)
        return if (hit != null) {
            val blockMin = Vec3f.of(hit.x.toFloat(), hit.y.toFloat(), hit.z.toFloat())
            val blockMax = Vec3f.of((hit.x + 1).toFloat(), (hit.y + 1).toFloat(), (hit.z + 1).toFloat())

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


}