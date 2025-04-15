package arc.demo.world

import arc.demo.world.block.Block
import arc.demo.world.chunk.Chunk
import arc.graphics.EmptyShaderInstance
import arc.model.Model
import arc.shader.ShaderInstance

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

    fun updateChunk(chunkX: Int, chunkY: Int, chunkZ: Int) {
        getChunk(chunkX, chunkZ)?.update(chunkY)
    }

    fun allChanged() {
        chunks.forEach {
            it.value.sections.forEach { section ->
                section?.update()
            }
        }
    }

    fun render(shader: ShaderInstance) {
        shader.bind()

        chunks.forEach {
            it.value.sections.forEach { section ->
                section?.handler?.render(EmptyShaderInstance)
            }
        }

        shader.unbind()
    }

}