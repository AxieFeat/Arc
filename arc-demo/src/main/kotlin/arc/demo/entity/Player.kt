package arc.demo.entity

import arc.demo.world.World
import arc.math.Vec3f

class Player(
    val world: World,
) {

    var distanceView = 8

    val position: Vec3f = Vec3f.of(0f, 0f, 0f)

    fun setPosition(x: Float, y: Float, z: Float) {
        position.apply {
            this.x = x
            this.y = y
            this.z = z
        }
    }

}