package arc.demo.world

import arc.demo.world.block.Block
import arc.demo.world.chunk.Chunk
import arc.demo.world.chunk.ChunkSection
import arc.model.Face
import arc.model.Model
import arc.model.cube.Cube
import arc.model.cube.CubeFace
import arc.model.texture.ModelTexture
import arc.shader.ShaderInstance
import de.articdive.jnoise.core.api.functions.Interpolation
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator

class World {

    private val noise = PerlinNoiseGenerator.newBuilder().setInterpolation(Interpolation.COSINE).build();
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
        if(y !in 0..255) {
            println("Can not block at [$y] height!")
            return
        }

        val chunkX = x shr 4
        val chunkZ = z shr 4
        val chunk = getOrCreateChunk(chunkX, chunkZ)
        chunk.setBlock(x and 15, y, z and 15, model)
    }

    fun setBlockAndUpdateChunk(x: Int, y: Int, z: Int, model: Model? = model()) {
        if(y !in 0..255) {
            println("Can not block at [$y] height!")
            return
        }

        val start = System.currentTimeMillis()
        val chunkX = x shr 4
        val chunkZ = z shr 4
        val chunk = getOrCreateChunk(chunkX, chunkZ)
        chunk.setBlockAndUpdateChunk(x and 15, y, z and 15, model)
        println("Block [$x:$y:$z] took ${System.currentTimeMillis() - start} ms")
    }

    fun updateChunkSection(chunkX: Int, chunkY: Int, chunkZ: Int) {
//        getChunk(chunkX, chunkZ)?.sections
    }

    fun allChanged() {
        var counter = 0
        chunks.values.forEach { chunk ->
            chunk.sections.forEach { section ->
                section?.rebuildModel()
                counter += section?.dispatcher?.cubes?.size ?: 0
            }
        }
        println("Blocks in world: $counter")
    }

    fun render(shader: ShaderInstance) {
        shader.bind()

//        val camera = VoxelGame.application.renderSystem.scene.camera

        chunks.values.forEach { chunk ->
            chunk.sections.forEach { section ->
                section?.dispatcher?.render()
            }
        }

        shader.unbind()
    }

    fun generateChunkAt(chunkX: Int, chunkZ: Int) {
        val start = System.currentTimeMillis()
        val scale = 0.05
        val maxHeight = 255

        val chunk = getOrCreateChunk(chunkX, chunkZ)
        val needUpdate = mutableSetOf<ChunkSection>()

        for (localX in 0 until 16) {
            for (localZ in 0 until 16) {
                val worldX = chunkX * 16 + localX
                val worldZ = chunkZ * 16 + localZ

                val height = (perlin(worldX * scale, worldZ * scale) * 10).toInt()
                    .coerceIn(1, maxHeight)

                for (y in 0 until height) {
                    needUpdate.add(
                        chunk.setBlock(worldX, y, worldZ, model())
                    )
                }
            }
        }

        needUpdate.forEach { section ->
            section.rebuildModel()
        }

        println("Chunk [${chunkX}:${chunkZ}] generated for ${System.currentTimeMillis() - start} ms!")
    }

    private fun perlin(x: Double, y: Double): Double {
        val value = noise.evaluateNoise(x, y)

        return (value + 1.0) / 2.0
    }

    private fun model() = Model.builder()
        .addCube(
            Cube.builder()
                .setFrom(0f, 0f, 0f)
                .setTo(1f, 1f, 1f)
                .addFace(Face.NORTH, defaultFace())
                .addFace(Face.SOUTH, defaultFace())
                .addFace(Face.WEST, defaultFace())
                .addFace(Face.EAST, defaultFace())
                .addFace(Face.UP, defaultFace())
                .addFace(Face.DOWN, defaultFace())
                .build()
        )
        .setTexture(
            ModelTexture.builder()
                .setImage("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAAAAAA6mKC9AAAAZElEQVR42jWNsQ0AMQjEGJcB3HsEr/xSkncBOgkfUyXIguoo4mEXGdkFFvBdlGmpwKCAWIWOeYFladCK4n2BXZZn6vR3dsXRfLnKWfFoXmtUrX81AiboaZ9u4I1G8KdqPICl6AdyLn2NfcJFIAAAAABJRU5ErkJggg==")
                .build()
        )
        .build()

    private fun defaultFace(): CubeFace {
        return CubeFace.builder()
            .setUvMin(0, 0)
            .setUvMax(16, 16)
            .build()
    }
}
