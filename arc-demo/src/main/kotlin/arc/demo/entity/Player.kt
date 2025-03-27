package arc.demo.entity

import arc.demo.VoxelGame
import arc.graphics.Camera
import arc.math.AABB
import arc.math.Vec3f

object Player {

    val camera: Camera = Camera.create(65f, VoxelGame.application.window.width.toFloat(), VoxelGame.application.window.height.toFloat())

    val aabb: AABB = AABB.of(Vec3f.ZERO, Vec3f.ZERO)

}