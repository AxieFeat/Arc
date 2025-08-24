package arc.demo.entity

import arc.demo.VoxelGame
import arc.demo.input.CameraControlBind
import arc.demo.screen.TerrainScreen.breakBlock
import arc.demo.screen.TerrainScreen.placeBlock
import arc.demo.world.World
import arc.demo.world.chunk.Chunk
import arc.graphics.Camera
import arc.input.KeyCode
import arc.input.keyboard
import arc.input.mouse
import arc.math.AABB
import org.joml.Vector3f
import kotlin.math.abs
import kotlin.math.min

class Player(
    val world: World,
    val camera: Camera,
) {

    var position: Vector3f = Vector3f()
    set(value) {
        field = value
        camera.position.set(
            position.x,
            position.y + 1.65f,
            position.z
        )
    }
    private val velocity = Vector3f(0f, 0f, 0f)

    var viewDistance = 8

    // Because world not have save function we need delete far chunks for memory saving.
    var memoryDistance = 32

    var fly = false

    private val sensitivity = 0.15f
    private var speed = 0f

    private val fovModifier = 1.1f
    private val fov = 85f
    private var targetFov = 0f
    private var currentFov = 0f
    private val fovLerpSpeed = 10f

    private var breakCooldown = 0f
    private val breakInterval = 0.2f

    private var placeCooldown = 0f
    private val placeInterval = 0.2f

    private var isGrounded = false

    private val playerSize = Vector3f(0.6f, 1.79f, 0.6f)
    private val gravity = -20f
    private val jumpVelocity = 8f
    private val terminalVelocity = -50f

    private fun getPlayerAABB(pos: Vector3f): AABB {
        val min = Vector3f(pos.x - playerSize.x, pos.y, pos.z - playerSize.z)
        val max = Vector3f(pos.x + playerSize.x, pos.y + playerSize.y, pos.z + playerSize.z)
        return AABB.of(min, max)
    }

    private fun collides(aabb: AABB): Boolean {
        val min = aabb.min
        val max = aabb.max

        for (x in min.x.toInt()..max.x.toInt()) {
            for (y in min.y.toInt()..max.y.toInt()) {
                for (z in min.z.toInt()..max.z.toInt()) {
                    val block = world.getBlock(x, y, z) ?: continue
                    val aabb = block.aabb ?: continue

                    if (aabb.intersects(aabb)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun resolveCollision(pos: Vector3f, delta: Vector3f): Vector3f {
        val newPos = Vector3f(pos).add(delta)
        val aabb = getPlayerAABB(newPos)

        if (!collides(aabb)) return delta

        val resolvedDelta = Vector3f(delta)

        val tempX = Vector3f(newPos.x, pos.y, pos.z)
        if (collides(getPlayerAABB(tempX))) {
            resolvedDelta.x = 0f
        }

        val tempY = Vector3f(pos.x, newPos.y, pos.z)
        if (collides(getPlayerAABB(tempY))) {
            resolvedDelta.y = 0f
            if (delta.y < 0f) isGrounded = true
        }

        val tempZ = Vector3f(pos.x, pos.y, newPos.z)
        if (collides(getPlayerAABB(tempZ))) {
            resolvedDelta.z = 0f
        }

        return resolvedDelta
    }

    fun handleInput(delta: Float) {
        val mouse = VoxelGame.application.mouse
        val keyboard = VoxelGame.application.keyboard

        speed = if (keyboard.isPressed(KeyCode.KEY_LCONTROL)) 7f else 4f
        speed *= if(fly) 10f else 1f
        targetFov = if (keyboard.isPressed(KeyCode.KEY_LCONTROL)) fov * fovModifier else fov
        currentFov += (targetFov - currentFov) * min(1f, fovLerpSpeed * delta)
        camera.fov = currentFov

        val cameraForward = Vector3f(camera.ray.direction.x, 0f, camera.ray.direction.z).normalize()
        val cameraRight = Vector3f(-cameraForward.z, 0f, cameraForward.x).normalize()

        val moveDir = Vector3f()
        if (keyboard.isPressed(KeyCode.KEY_W)) moveDir.add(cameraForward)
        if (keyboard.isPressed(KeyCode.KEY_S)) moveDir.sub(cameraForward)
        if (keyboard.isPressed(KeyCode.KEY_A)) moveDir.add(cameraRight.negate())
        if (keyboard.isPressed(KeyCode.KEY_D)) moveDir.sub(cameraRight.negate())

        if (fly) {
            if (keyboard.isPressed(KeyCode.KEY_SPACE)) moveDir.y += 1f
            if (keyboard.isPressed(KeyCode.KEY_LSHIFT)) moveDir.y -= 1f

            if (moveDir.lengthSquared() > 0f) {
                moveDir.normalize().mul(speed)
            }

            position = position.add(moveDir.mul(delta))
        } else {
            if (moveDir.lengthSquared() > 0f) {
                moveDir.normalize().mul(speed)
            }

            velocity.x = moveDir.x
            velocity.z = moveDir.z

            if (isGrounded && keyboard.isPressed(KeyCode.KEY_SPACE)) {
                velocity.y = jumpVelocity
                isGrounded = false
            }

            val maxStep = 0.05f
            var remainingTime = delta
            while (remainingTime > 0f) {
                val step = min(remainingTime, maxStep)
                remainingTime -= step

                velocity.y += gravity * step
                velocity.y = velocity.y.coerceAtLeast(terminalVelocity)

                val movement = Vector3f(
                    velocity.x * step,
                    velocity.y * step,
                    velocity.z * step
                )

                val resolved = resolveCollision(position, movement)

                position = position.add(resolved)
            }

            if (collides(getPlayerAABB(position))) {
                for (i in 1..5) {
                    position.y += 0.1f
                    if (!collides(getPlayerAABB(position))) break
                }
            }
        }

        if (CameraControlBind.status) {
            val mouseDeltaX = mouse.displayVec.x
            val mouseDeltaY = mouse.displayVec.y

            camera.rotate(-mouseDeltaY * sensitivity, mouseDeltaX * sensitivity, 0f)
        }

        if (mouse.isPressed(KeyCode.MOUSE_LEFT)) {
            breakCooldown -= delta
            if (breakCooldown <= 0f) {
                breakBlock()
                breakCooldown = breakInterval
            }
        } else breakCooldown = 0f

        if (mouse.isPressed(KeyCode.MOUSE_RIGHT)) {
            placeCooldown -= delta
            if (placeCooldown <= 0f) {
                placeBlock()
                placeCooldown = placeInterval
            }
        } else placeCooldown = 0f

        camera.update()
        mouse.reset()
    }

    fun shouldRender(chunk: Chunk, distance: Int = viewDistance): Boolean {
        val playerChunkX = (position.x.toInt() shr 4)
        val playerChunkZ = (position.z.toInt() shr 4)

        val dx = chunk.x - playerChunkX
        val dz = chunk.z - playerChunkZ

        return abs(dx) <= distance && abs(dz) <= distance
    }

    fun shouldDelete(chunk: Chunk, distance: Int = memoryDistance): Boolean {
        return !shouldRender(chunk, distance)
    }

    fun shouldLoad(): List<Pair<Int, Int>> {
        val playerChunkX = (position.x.toInt() shr 4)
        val playerChunkZ = (position.z.toInt() shr 4)

        val chunksToLoad = mutableListOf<Pair<Int, Int>>()

        for (dz in -viewDistance..viewDistance) {
            for (dx in -viewDistance..viewDistance) {
                val chunkX = playerChunkX + dx
                val chunkZ = playerChunkZ + dz

                if (dx * dx + dz * dz <= viewDistance * viewDistance) {
                    chunksToLoad.add(chunkX to chunkZ)
                }
            }
        }

        return chunksToLoad
    }


}
