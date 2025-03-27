package arc.demo.world.block

import arc.demo.world.chunk.ChunkSection
import arc.texture.TextureAtlas

class Block(
    val texture: TextureAtlas,
    val section: ChunkSection,
    val x: Int,
    val y: Int,
    val z: Int,
) {

    var faces = mutableListOf<BlockFace>()

    fun update() {
        faces.clear()
        faces.addAll(section.chunk.world.facesForRender(this))
    }

}