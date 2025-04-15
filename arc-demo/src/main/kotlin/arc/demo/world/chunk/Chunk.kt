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
        val section = getSection(sectionIndex) ?: return null
        return section.getBlock(x and 15, y and 15, z and 15)
    }

    fun setBlock(x: Int, y: Int, z: Int, model: Model?) {
        val sectionIndex = y shr 4
        val section = getOrCreateSection(sectionIndex)
        section.setBlock(x and 15, y and 15, z and 15, model)
    }

    fun update(y: Int) {
        sections[y]?.update()
    }

}