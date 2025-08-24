package arc.demo.world.block

import arc.math.AABB
import arc.model.Model

abstract class Block(
    val id: Int,
    val model: Model?,
    val aabb: AABB?,
)