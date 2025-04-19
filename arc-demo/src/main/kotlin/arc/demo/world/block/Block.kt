package arc.demo.world.block

import arc.demo.world.chunk.ChunkSection
import arc.graphics.DrawBuffer
import arc.math.AABB
import arc.model.Face
import arc.model.Model
import arc.texture.TextureAtlas

data class Block(
    val section: ChunkSection,
    val x: Int,
    val y: Int,
    val z: Int,
    val model: Model,
    val aabb: AABB
)