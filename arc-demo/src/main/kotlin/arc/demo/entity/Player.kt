package arc.demo.entity

import arc.demo.VoxelGame
import arc.demo.input.CameraControlBind
import arc.demo.screen.TerrainScreen.breakBlock
import arc.demo.screen.TerrainScreen.placeBlock
import arc.demo.world.World
import arc.graphics.Camera
import arc.input.KeyCode
import arc.math.Point3d
import arc.math.Vec3f
import arc.math.AABB
import org.joml.Vector3f
import kotlin.math.min

class Player(
    val world: World,
    val camera: Camera,
) {

    val position: Point3d = Point3d.of(0.0, 0.0, 0.0)
    private val velocity = Vector3f(0f, 0f, 0f)

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

    private val playerSize = Vec3f.of(0.3f, 0.9f, 0.3f)
    private val gravity = -20f
    private val jumpVelocity = 8f
    private val terminalVelocity = -50f

    fun setPosition(x: Double, y: Double, z: Double) {
        position.apply {
            this.x = x
            this.y = y
            this.z = z
        }

        camera.position.apply {
            this.x = position.x
            this.y = position.y + 1.65
            this.z = position.z
        }
    }


    private fun getPlayerAABB(pos: Vector3f): AABB {
        val min = Vec3f.of(pos.x - playerSize.x, pos.y, pos.z - playerSize.z)
        val max = Vec3f.of(pos.x + playerSize.x, pos.y + playerSize.y, pos.z + playerSize.z)
        return AABB.of(min, max)
    }

    private fun collides(aabb: AABB): Boolean {
        val min = aabb.min
        val max = aabb.max

        for (x in min.x.toInt()..max.x.toInt()) {
            for (y in min.y.toInt()..max.y.toInt()) {
                for (z in min.z.toInt()..max.z.toInt()) {
                    val block = world.getBlock(x, y, z) ?: continue
                    if (aabb.intersects(block.aabb)) {
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
            if (delta.y < 0) isGrounded = true
        }

        val tempZ = Vector3f(pos.x, pos.y, newPos.z)
        if (collides(getPlayerAABB(tempZ))) {
            resolvedDelta.z = 0f
        }

        return resolvedDelta
    }

    fun handleInput(delta: Float) {
        val application = VoxelGame.application
        val input = application.keyboard

        speed = if (input.isPressed(KeyCode.KEY_LCONTROL)) 7f else 4f
        targetFov = if (input.isPressed(KeyCode.KEY_LCONTROL)) fov * fovModifier else fov
        currentFov += (targetFov - currentFov) * min(1f, fovLerpSpeed * delta)
        camera.fov = currentFov

        val cameraForward = Vector3f(
            camera.ray.direction.x,
            0f,
            camera.ray.direction.z
        ).normalize()

        val cameraRight = Vector3f(-cameraForward.z, 0f, cameraForward.x).normalize()

        val moveDir = Vector3f()
        if (input.isPressed(KeyCode.KEY_W)) moveDir.add(cameraForward)
        if (input.isPressed(KeyCode.KEY_S)) moveDir.sub(cameraForward)
        if (input.isPressed(KeyCode.KEY_A)) moveDir.add(cameraRight.negate())
        if (input.isPressed(KeyCode.KEY_D)) moveDir.sub(cameraRight.negate())

        if (moveDir.lengthSquared() > 0f) {
            moveDir.normalize().mul(speed)
        }

        velocity.x = moveDir.x
        velocity.z = moveDir.z

        if (isGrounded && input.isPressed(KeyCode.KEY_SPACE)) {
            velocity.y = jumpVelocity
            isGrounded = false
        }

        velocity.y += gravity * delta
        velocity.y = velocity.y.coerceAtLeast(terminalVelocity)

        val pos = Vector3f(position.x.toFloat(), position.y.toFloat(), position.z.toFloat())
        val movement = Vector3f(
            velocity.x * delta,
            velocity.y * delta,
            velocity.z * delta
        )

        val resolvedMovement = resolveCollision(pos, movement)
        pos.add(resolvedMovement)

        if (collides(getPlayerAABB(pos))) {
            for (i in 1..5) {
                pos.y += 0.1f
                if (!collides(getPlayerAABB(pos))) break
            }
        }

        setPosition(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())

        if (CameraControlBind.status) {
            val mouseDeltaX = application.mouse.displayVec.x
            val mouseDeltaY = application.mouse.displayVec.y

            camera.rotate(
                -mouseDeltaY * sensitivity,
                mouseDeltaX * sensitivity,
                0f
            )
        }

        if (application.mouse.isPressed(KeyCode.MOUSE_LEFT)) {
            breakCooldown -= delta
            if (breakCooldown <= 0f) {
                breakBlock()
                breakCooldown = breakInterval
            }
        } else breakCooldown = 0f

        if (application.mouse.isPressed(KeyCode.MOUSE_RIGHT)) {
            placeCooldown -= delta
            if (placeCooldown <= 0f) {
                placeBlock()
                placeCooldown = placeInterval
            }
        } else placeCooldown = 0f

        camera.update()
        application.mouse.reset()
    }
}
