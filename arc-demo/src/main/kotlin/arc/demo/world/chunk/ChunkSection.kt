package arc.demo.world.chunk

import arc.demo.world.block.Block

class ChunkSection(
    val chunk: Chunk
) {

    val blocks: MutableList<MutableList<MutableList<Block>>> = mutableListOf()

    fun updateAll() {
        blocks.forEach {
            it.forEach {
                it.forEach { block ->
                    block.update()
                }
            }
        }
    }

    fun update(x: Int, y: Int, z: Int) {
        blocks[x][y][z].update()
    }

    fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        blocks[x][y][z] = block
    }

    fun getBlock(x: Int, y: Int, z: Int): Block {
        return blocks[x][y][z]
    }

}