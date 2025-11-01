package arc.demo.world

import arc.demo.entity.Player
import arc.demo.world.block.Block
import arc.demo.world.block.Blocks
import arc.demo.world.chunk.Chunk
import arc.shader.ShaderInstance
import de.articdive.jnoise.core.api.functions.Interpolation
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator
import kotlin.collections.forEach

class World {
    private val noise = PerlinNoiseGenerator.newBuilder().setInterpolation(Interpolation.COSINE).build();

    private val chunks = HashMap<Long, Chunk>(1024)

    // Ключ чанка по координатам (x,z)
    private fun key(cx: Int, cz: Int): Long {
        return (cx.toLong() shl 32) or (cz.toLong() and 0xFFFFFFFFL)
    }

    fun isChunkLoaded(chunkX: Int, chunkZ: Int): Boolean =
        chunks.containsKey(key(chunkX, chunkZ))

    fun getChunk(chunkX: Int, chunkZ: Int): Chunk =
        chunks.getOrPut(key(chunkX, chunkZ)) { Chunk(this, chunkX, chunkZ) }

    fun getBlock(worldX: Int, worldY: Int, worldZ: Int): Block {
        val chunkX = worldX shr 4
        val chunkZ = worldZ shr 4
        val localX = worldX and 15
        val localZ = worldZ and 15
        return getChunk(chunkX, chunkZ).getBlock(localX, worldY, localZ)
    }

    fun setBlock(worldX: Int, worldY: Int, worldZ: Int, block: Block) {
        val chunkX = worldX shr 4
        val chunkZ = worldZ shr 4
        val localX = worldX and 15
        val localZ = worldZ and 15
        getChunk(chunkX, chunkZ).setBlock(localX, worldY, localZ, block)
    }

    fun setBlockAndNotify(worldX: Int, worldY: Int, worldZ: Int, block: Block) {
        val chunkX = worldX shr 4
        val chunkZ = worldZ shr 4
        val localX = worldX and 15
        val localZ = worldZ and 15
        getChunk(chunkX, chunkZ).setBlockAndNotify(localX, worldY, localZ, block)
    }

    // Пример: перебрать все загруженные чанки (для вашего рендера/обновлений и т.д.)
    fun forEachChunk(action: (Chunk) -> Unit) {
        chunks.values.forEach(action)
    }

    fun rebuild() {
        chunks.values.toList().forEach { chunk ->
            chunk.sections.toList().forEach { s -> s?.renderer?.rebuild() }
        }
    }

    fun render() {
        forEachChunk { chunk ->
            chunk.forEachNonEmptySection { section ->
                section.renderer.render()
            }
        }
    }

    fun generateChunkAt(chunkX: Int, chunkZ: Int) {
        val scale = 0.05
        val maxHeight = 255

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
    }

    private fun perlin(x: Double, y: Double): Double {
        val value = noise.evaluateNoise(x, y)

        return (value + 1.0) / 2.0
    }
}

