package arc.demo.world.block

import arc.demo.world.chunk.ChunkSection
import arc.graphics.DrawBuffer
import arc.model.Face
import arc.texture.TextureAtlas

class Block(
    val texture: TextureAtlas,
    val section: ChunkSection,
    val x: Int,
    val y: Int,
    val z: Int,
) {

    var viewableFaces = mutableListOf<Face>()

    fun update(buffer: DrawBuffer) {
        viewableFaces.clear()
    }

}