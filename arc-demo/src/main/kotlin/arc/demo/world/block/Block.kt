package arc.demo.world.block

import arc.demo.world.chunk.ChunkSection
import arc.math.AABB
import arc.model.Model

data class Block(
    val section: ChunkSection,
    val x: Int,
    val y: Int,
    val z: Int,
    val model: Model,
    val aabb: AABB
)