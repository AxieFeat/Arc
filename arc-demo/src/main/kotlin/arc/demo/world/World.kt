package arc.demo.world

import arc.demo.VoxelGame
import arc.demo.shader.VertexFormatContainer
import arc.demo.world.block.Block
import arc.demo.world.chunk.Chunk
import arc.graphics.DrawerMode
import arc.graphics.EmptyShaderInstance
import arc.model.Model
import arc.shader.ShaderInstance
import arc.util.Color
import kotlin.use

class World {

    private val chunks = mutableMapOf<Pair<Int, Int>, Chunk>()

    fun getChunk(chunkX: Int, chunkZ: Int): Chunk? {
        return chunks[chunkX to chunkZ]
    }

    fun getOrCreateChunk(chunkX: Int, chunkZ: Int): Chunk {
        return chunks.getOrPut(chunkX to chunkZ) { Chunk(this, chunkX, chunkZ) }
    }

    fun getBlock(x: Int, y: Int, z: Int): Block? {
        val chunkX = x shr 4
        val chunkZ = z shr 4
        val chunk = getChunk(chunkX, chunkZ) ?: return null
        return chunk.getBlock(x and 15, y, z and 15)
    }

    fun setBlock(x: Int, y: Int, z: Int, model: Model?) {
        val chunkX = x shr 4
        val chunkZ = z shr 4
        val chunk = getOrCreateChunk(chunkX, chunkZ)
        chunk.setBlock(x and 15, y, z and 15, model)
    }

    fun setBlockAndUpdateChunk(x: Int, y: Int, z: Int, model: Model?) {
        val chunkX = x shr 4
        val chunkZ = z shr 4
        val chunk = getOrCreateChunk(chunkX, chunkZ)
        chunk.setBlock(x and 15, y, z and 15, model)
        chunk.rebuildModel()
    }

    fun updateChunk(chunkX: Int, chunkZ: Int) {
        getChunk(chunkX, chunkZ)?.rebuildModel()
    }

    fun allChanged() {
        var counter = 0
        chunks.values.forEach { chunk ->
            chunk.rebuildModel()
            counter += chunk.handler?.model?.cubes?.size ?: 0
        }
        println("Blocks in world: $counter")
    }

    fun render(shader: ShaderInstance) {
        shader.bind()

        val camera = VoxelGame.application.renderSystem.scene.camera

        chunks.values.forEach { chunk ->
            chunk.handler?.let { handler ->
                if (camera.frustum.isBoxInFrustum(handler.aabb)) {
                    handler.render(shader)
                }
            }
        }

        shader.unbind()
    }
}
