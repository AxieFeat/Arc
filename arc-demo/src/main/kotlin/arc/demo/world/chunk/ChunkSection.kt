package arc.demo.world.chunk

import arc.demo.world.block.Block
import arc.model.Model

class ChunkSection(val chunk: Chunk) {

    val blocks = arrayOfNulls<Block>(16 * 16 * 16)

    fun getBlock(x: Int, y: Int, z: Int): Block? {
        return blocks[getIndex(x, y, z)]
    }

    fun setBlock(x: Int, y: Int, z: Int, model: Model?) {
        val index = getIndex(x, y, z)
        if (model == null) {
            blocks[index] = null
        } else {
            val sectionY = chunk.sections.indexOf(this)
            val worldX = chunk.x * 16 + x
            val worldY = sectionY * 16 + y
            val worldZ = chunk.z * 16 + z

            model.translate(worldX.toFloat(), worldY.toFloat(), worldZ.toFloat())
            blocks[index] = Block(this, worldX, worldY, worldZ, model)
        }
    }

    private fun getIndex(x: Int, y: Int, z: Int): Int {
        require(x in 0..15 && y in 0..15 && z in 0..15)
        return (y shl 8) or (z shl 4) or x
    }

    fun isEmpty(): Boolean = blocks.all { it == null }
}
