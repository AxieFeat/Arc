package arc.demo.world.chunk

import arc.demo.VoxelGame
import arc.demo.shader.VertexFormatContainer
import arc.demo.world.block.Block
import arc.graphics.DrawerMode

class ChunkSection(
    val chunk: Chunk
) {

    private val buffer = VoxelGame.application.renderSystem.drawer.begin(DrawerMode.TRIANGLES, VertexFormatContainer.positionTex, 1024)

    val blocks: MutableList<MutableList<MutableList<Block>>> = mutableListOf()

    fun updateAll() {
        buffer.clear()
        blocks.forEach {
            it.forEach {
                it.forEach { block ->
                    block.update(buffer)
                }
            }
        }
    }

    fun update(x: Int, y: Int, z: Int) {
        blocks[x][y][z].update(buffer)
    }

    fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        blocks[x][y][z] = block
    }

    fun getBlock(x: Int, y: Int, z: Int): Block? {
        return blocks[x][y][z]
    }

}