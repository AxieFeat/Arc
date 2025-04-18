package arc.demo.world.chunk

import arc.demo.VoxelGame
import arc.demo.world.World
import arc.demo.world.block.Block
import arc.graphics.ModelHandler
import arc.model.Model

class Chunk(
    val world: World,
    val x: Int,
    val z: Int
) {

    val sections = Array<ChunkSection?>(16) { null }

    var handler: ModelHandler? = null

    fun getSection(y: Int): ChunkSection? {
        return if (y in 0 until 16) sections[y] else null
    }

    fun getOrCreateSection(y: Int): ChunkSection {
        return sections[y] ?: ChunkSection(this).also { sections[y] = it }
    }

    fun getBlock(x: Int, y: Int, z: Int): Block? {
        val sectionIndex = y shr 4
        return getSection(sectionIndex)?.getBlock(x and 15, y and 15, z and 15)
    }

    fun setBlock(x: Int, y: Int, z: Int, model: Model?) {
        val sectionIndex = y shr 4
        val section = getOrCreateSection(sectionIndex)
        section.setBlock(x and 15, y and 15, z and 15, model)
    }

    fun rebuildModel() {
        handler?.cleanup()

        val builder = Model.builder()

        for (section in sections) {
            if (section == null || section.isEmpty()) continue


            val blocks = section.blocks.filterNotNull()
            if (blocks.isEmpty()) continue

            blocks.forEach { block ->
                builder.addCube(*block.model.cubes.map { it.copy() }.toTypedArray())
            }
        }

        val firstBlock = sections.firstNotNullOfOrNull { it?.blocks?.firstOrNull { b -> b != null } }
        firstBlock?.let {
            builder.setTexture(it.model.texture)
        }

        val model = builder.build()

        model.cullFaces()

        println("Blocks in chunk: ${model.cubes.size}")

        handler = ModelHandler.of(VoxelGame.application.renderSystem.drawer, model)
    }
}