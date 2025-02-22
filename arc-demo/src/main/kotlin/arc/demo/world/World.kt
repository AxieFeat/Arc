package arc.demo.world

import arc.graphics.g3d.ChunkContainer
import arc.graphics.g3d.ChunkSection

class World : ChunkContainer {

    private val chunks = mutableSetOf<ChunkSection>()

    override fun get(x: Int, z: Int): ChunkSection {
        return chunks.find { it.xOffset == x && it.zOffset == z } ?: Chunk(x, z).also { chunks.add(it) }
    }

    override fun updateChunk(x: Int, z: Int) {
        get(x, z).update()
    }

}