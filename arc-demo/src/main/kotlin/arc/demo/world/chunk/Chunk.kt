package arc.demo.world.chunk

import arc.demo.entity.Player
import arc.demo.world.World
import arc.demo.world.block.Block
import arc.demo.world.block.Blocks
import arc.graphics.Camera

class Chunk(
    val world: World,
    val x: Int,
    val z: Int
) {
    // 0..15 секций по высоте => мир высотой 256
    val sections: Array<ChunkSection?> = arrayOfNulls(16)

    // Палитра на чанк: Block -> Short и обратно
    private val blockToIndex = HashMap<Block, Short>(64)
    private val indexToBlock = HashMap<Short, Block>(64)
    private var nextPaletteId: Short = 0

    // Хук для системы рендера: вы можете подвязать пересборку мешей секции
    var onSectionChanged: ((ChunkSection) -> Unit)? = null

    init {
        // Гарантируем, что AIR = 0
        addToPalette(Blocks.AIR)
    }

    fun getSection(sectionY: Int): ChunkSection {
        require(sectionY in 0..15) { "sectionY must be in 0..15" }
        val existing = sections[sectionY]
        if (existing != null) return existing
        val created = ChunkSection(this, sectionY)
        sections[sectionY] = created
        return created
    }

    fun getBlock(localX: Int, worldY: Int, localZ: Int): Block {
        require(localX in 0..15 && localZ in 0..15 && worldY in 0..255)
        val secY = worldY shr 4
        val ly = worldY and 15
        val section = sections[secY] ?: return Blocks.AIR
        val p = section.getBlockIndex(localX, ly, localZ)
        return getBlockByPaletteIndex(p)
    }

    fun setBlock(localX: Int, worldY: Int, localZ: Int, block: Block) {
        require(localX in 0..15 && localZ in 0..15 && worldY in 0..255)
        val secY = worldY shr 4
        val ly = worldY and 15
        val section = getSection(secY)
        val idx = addToPalette(block)
        section.setBlockIndex(localX, ly, localZ, idx)
    }

    fun setBlockAndNotify(localX: Int, worldY: Int, localZ: Int, block: Block) {
        require(localX in 0..15 && localZ in 0..15 && worldY in 0..255)
        val secY = worldY shr 4
        val ly = worldY and 15
        val section = getSection(secY)
        val idx = addToPalette(block)
        section.setBlockIndexAndUpdate(localX, ly, localZ, idx)
    }

    fun getBlockByPaletteIndex(index: Short): Block =
        indexToBlock[index] ?: Blocks.AIR

    private fun addToPalette(block: Block): Short {
        blockToIndex[block]?.let { return it }
        val id = nextPaletteId++
        blockToIndex[block] = id
        indexToBlock[id] = block
        return id
    }

    fun forEachNonEmptySection(action: (ChunkSection) -> Unit) {
        sections.forEach { s -> if (s != null && !s.isEmpty) action(s) }
    }
}

