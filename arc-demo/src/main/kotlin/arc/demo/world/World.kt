package arc.demo.world

import arc.demo.entity.Player
import arc.demo.world.block.Block
import arc.demo.world.block.Blocks
import arc.demo.world.chunk.Chunk
import arc.shader.ShaderInstance
import de.articdive.jnoise.core.api.functions.Interpolation
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator

class World {

    private val noise = PerlinNoiseGenerator.newBuilder().setInterpolation(Interpolation.COSINE).build();

    private val chunks: MutableMap<Long, Chunk> = mutableMapOf()

    private fun getChunkKey(chunkX: Int, chunkZ: Int): Long {
        return (chunkX.toLong() shl 32) or (chunkZ.toLong() and 0xFFFFFFFFL)
    }

    fun isChunkExist(chunkX: Int, chunkZ: Int): Boolean {
        return chunks[getChunkKey(chunkX, chunkZ)] != null
    }

    fun getChunk(chunkX: Int, chunkZ: Int): Chunk {
        return chunks.getOrPut(getChunkKey(chunkX, chunkZ)) { Chunk(this, chunkX, chunkZ) }
    }

    fun getBlock(worldX: Int, worldY: Int, worldZ: Int): Block {
        val chunk = getChunk(worldX shr 4, worldZ shr 4)
        return chunk.getBlock(worldX and 15, worldY, worldZ and 15)
    }

    fun setBlock(worldX: Int, worldY: Int, worldZ: Int, state: Block) {
        val chunk = getChunk(worldX shr 4, worldZ shr 4)
        chunk.setBlock(worldX and 15, worldY, worldZ and 15, state)
    }

    fun setBlockAndUpdate(worldX: Int, worldY: Int, worldZ: Int, state: Block) {
        val chunk = getChunk(worldX shr 4, worldZ shr 4)
        chunk.setBlockAndUpdate(worldX and 15, worldY, worldZ and 15, state)
    }

    fun render(shader: ShaderInstance, player: Player) {
        shader.bind()

        player.shouldLoad().forEach {
            if(!isChunkExist(it.first, it.second)) {
                generateChunkAt(it.first, it.second)
            }
        }

        chunks.values.forEach { chunk ->
            chunk.render(player.camera, player)
        }

        shader.unbind()
    }

    fun generateChunkAt(chunkX: Int, chunkZ: Int) {
        val start = System.nanoTime()
        val scale = 0.05
        val maxHeight = 255

        val chunk = getChunk(chunkX, chunkZ)

        for (localX in 0..16) {
            for (localZ in 0..16) {
                val worldX = chunkX * 16 + localX
                val worldZ = chunkZ * 16 + localZ

                val height = (perlin(worldX * scale, worldZ * scale) * 10).toInt()
                    .coerceIn(1, maxHeight)

                for (y in 0..height) {
                    setBlock(worldX, y, worldZ, Blocks.STONE)
                }
            }
        }

        chunk.update()

        val resultTime = System.nanoTime() - start
        println("Chunk [${chunkX}:${chunkZ}] generated for ${resultTime / 1000000} ms! ($resultTime ns)")

    }

    private fun perlin(x: Double, y: Double): Double {
        val value = noise.evaluateNoise(x, y)

        return (value + 1.0) / 2.0
    }

}
