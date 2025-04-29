package arc.demo.world.block

import arc.demo.world.chunk.ChunkSection
import arc.graphics.DrawBuffer
import arc.math.AABB
import arc.math.Vec2f
import arc.model.Face
import arc.model.Model
import arc.model.cube.CubeFace
import arc.texture.TextureAtlas
import arc.util.Color

data class Block(
    val section: ChunkSection,
    val x: Int,
    val y: Int,
    val z: Int,
    val model: Model,
    val aabb: AABB
)