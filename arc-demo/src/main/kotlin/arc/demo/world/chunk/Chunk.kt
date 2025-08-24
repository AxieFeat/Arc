package arc.demo.world.chunk

import arc.demo.entity.Player
import arc.demo.world.World
import arc.demo.world.block.Block
import arc.demo.world.block.Blocks
import arc.graphics.Camera


class Chunk(
    val world: World,
    val x: Int,
    val z: Int,
) {

    private val sections = arrayOfNulls<ChunkSection>(16)
    private val paletteMap = mutableMapOf<Block, Short>()
    private val indexToBlock = mutableMapOf<Short, Block>()
    private var nextPaletteId: Short = 0

    init {
        addToPalette(Blocks.AIR)
    }

    fun getSectionBlocks(sectionY: Int): Array<Array<Array<Block>>> {
        return getSection(sectionY).getAllBlocks(indexToBlock)
    }

    fun getBlock(x: Int, y: Int, z: Int): Block {
        val sectionY = y shr 4
        val section = getSection(sectionY)
        val paletteIndex = section.getBlockIndex(x, y and 15, z)
        return indexToBlock[paletteIndex] ?: Blocks.AIR
    }

    fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        val sectionY = y shr 4
        val section = getSection(sectionY)
        val paletteIndex = addToPalette(block)
        section.setBlockIndex(x, y and 15, z, paletteIndex)
    }

    fun setBlockAndUpdate(x: Int, y: Int, z: Int, block: Block) {
        val sectionY = y shr 4
        val section = getSection(sectionY)
        val paletteIndex = addToPalette(block)
        section.setBlockIndex(x, y and 15, z, paletteIndex)
        section.update()
    }

    fun getSection(sectionY: Int): ChunkSection {
        require(sectionY in 0..15) { "Section Y must be between 0 and 15" }

        return sections[sectionY] ?: ChunkSection(this, sectionY).also { sections[sectionY] = it }
    }

    private fun addToPalette(block: Block): Short {
        if (paletteMap.containsKey(block)) {
            return paletteMap[block]!!
        }

        val id = nextPaletteId++
        paletteMap.put(block, id)
        indexToBlock.put(id, block)
        return id
    }

    fun update() {
        sections.forEach {
            it?.update()
        }
    }

    fun render(camera: Camera, player: Player) {
        sections.forEach { section ->
            if (section != null && !section.isEmpty) {
                if (player.shouldRender(this)) {

                    if (camera.frustum.isBoxInFrustum(section.aabb)) {
                        section.renderer.render()
                    }
                }
            }
        }
    }

}