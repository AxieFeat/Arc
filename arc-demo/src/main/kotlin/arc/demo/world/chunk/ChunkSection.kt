package arc.demo.world.chunk

import arc.demo.world.block.Block
import arc.demo.world.block.Blocks
import arc.math.AABB
import org.joml.Vector3f

class ChunkSection(
    val chunk: Chunk,
    val y: Int
) {

    private val blockIndices = ShortArray(4096)
    var isEmpty = true

    val aabb = AABB.of(
        Vector3f(
            (chunk.x * 16).toFloat(),
            (y * 16).toFloat() + 16f,
            (chunk.z * 16).toFloat()
        ),
        Vector3f(
            (chunk.x * 16).toFloat() + 16f,
            (y * 16).toFloat() + 32f,
            (chunk.z * 16).toFloat() + 16f
        )
    )

    val renderer = ChunkSectionRenderer(this)

    fun getBlockIndex(x: Int, y: Int, z: Int): Short {
        return blockIndices[(y shl 8) or (z shl 4) or x]
    }

    fun setBlockIndex(x: Int, y: Int, z: Int, index: Short) {
        blockIndices[(y shl 8) or (z shl 4) or x] = index
        if (index != 0.toShort()) isEmpty = false
    }

    fun setBlockIndexAndUpdate(x: Int, y: Int, z: Int, index: Short) {
        setBlockIndex(x, y, z, index)

        update()
    }

    fun getAllBlocks(palette: Map<Short, Block>): Array<Array<Array<Block>>> {
        val blocks = Array(16) { Array(16) { Array(16) { Blocks.AIR } } }

        if (isEmpty) return blocks

        for (y in 0..15) {
            for (z in 0..15) {
                for (x in 0..15) {
                    val index = blockIndices[(y shl 8) or (z shl 4) or x]

                    blocks[y][z][x] = palette[index] ?: Blocks.AIR
                }
            }
        }

        return blocks
    }

    fun update() {
        renderer.rebuild()
    }

}
