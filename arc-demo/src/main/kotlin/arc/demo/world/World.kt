package arc.demo.world

import arc.demo.VoxelGame
import arc.demo.shader.VertexFormatContainer
import arc.demo.world.block.Block
import arc.demo.world.block.BlockFace
import arc.demo.world.chunk.Chunk
import arc.graphics.DrawerMode

class World {

    val buffer = VoxelGame.application.renderSystem.drawer.begin(DrawerMode.TRIANGLES, VertexFormatContainer.positionTex, 1024)

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

    fun facesForRender(block: Block): List<BlockFace> {
        return listOf()
    }

    fun render() {
        VoxelGame.application.renderSystem.drawer.draw(buffer.build())
    }

}