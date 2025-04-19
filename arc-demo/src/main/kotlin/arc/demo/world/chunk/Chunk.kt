package arc.demo.world.chunk

import arc.demo.world.World
import arc.demo.world.block.Block
import arc.model.Model

class Chunk(
    val world: World,
    val x: Int,
    val z: Int
) {

    val sections = Array<ChunkSection?>(16) { null }

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

    fun setBlockAndUpdateChunk(x: Int, y: Int, z: Int, model: Model?) {
        val sectionIndex = y shr 4
        val section = getOrCreateSection(sectionIndex)
        section.setBlock(x and 15, y and 15, z and 15, model)
        section.rebuildModel()

        if (x and 15 == 0) {
            world.getChunk(this.x - 1, this.z)
                ?.getSection(sectionIndex)
                ?.rebuildModel()
        } else if (x and 15 == 15) {
            world.getChunk(this.x + 1, this.z)
                ?.getSection(sectionIndex)
                ?.rebuildModel()
        }

        if (z and 15 == 0) {
            world.getChunk(this.x, this.z - 1)
                ?.getSection(sectionIndex)
                ?.rebuildModel()
        } else if (z and 15 == 15) {
            world.getChunk(this.x, this.z + 1)
                ?.getSection(sectionIndex)
                ?.rebuildModel()
        }

        if (y and 15 == 0 && sectionIndex > 0) {
            getSection(sectionIndex - 1)?.rebuildModel()
        } else if (y and 15 == 15 && sectionIndex < 15) {
            getSection(sectionIndex + 1)?.rebuildModel()
        }
    }

    fun setBlock(x: Int, y: Int, z: Int, model: Model?): ChunkSection {
        val sectionIndex = y shr 4
        val section = getOrCreateSection(sectionIndex)
        section.setBlock(x and 15, y and 15, z and 15, model)

        return section
    }
}