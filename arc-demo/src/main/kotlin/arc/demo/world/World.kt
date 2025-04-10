package arc.demo.world

import arc.demo.world.block.Block
import arc.demo.world.chunk.Chunk

class World {

    val chunks = mutableListOf<Chunk>()

    fun updateAll() {
        chunks.forEach { it.updateAll() }
    }

    fun update(x: Int, z: Int) {
        chunks.find { it.x == x && it.z == z }?.updateAll()
    }

    fun getChunk(x: Int, z: Int): Chunk? {
        return chunks.find { it.x == x && it.z == z }
    }

    fun getBlock(x: Int, y: Int, z: Int): Block? {
        chunks.forEach { chunk ->
            chunk.sections.forEach {
                val block = it.getBlock(x, y, z)

                if(block != null) return block
            }
        }

        return null
    }

}