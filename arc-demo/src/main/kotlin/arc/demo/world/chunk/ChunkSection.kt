package arc.demo.world.chunk

import arc.demo.VoxelGame
import arc.demo.world.WorldModelDispatcher
import arc.demo.world.block.Block
import arc.math.AABB
import arc.model.Model
import org.joml.Vector3f

class ChunkSection(
    val chunk: Chunk
)  {

    val blocks = arrayOfNulls<Block>(16 * 16 * 16)
    var dispatcher: WorldModelDispatcher? = null
    val aabb = AABB.of(
        Vector3f((chunk.x * 16).toFloat(), (chunk.sections.indexOf(this) * 16).toFloat() + 16f, (chunk.z * 16).toFloat()),
        Vector3f((chunk.x * 16).toFloat() + 16f, (chunk.sections.indexOf(this) * 16).toFloat() + 32f, (chunk.z * 16).toFloat() + 16f)
    )

    var isLoaded = false
        private set

    fun getBlock(x: Int, y: Int, z: Int): Block? {
        return blocks[getIndex(x, y, z)]
    }

    fun setBlock(x: Int, y: Int, z: Int, model: Model?) {
        val index = getIndex(x, y, z)
        if (model == null) {
            blocks[index] = null
        } else {
            val sectionY = chunk.sections.indexOf(this)
            val worldX = chunk.x * 16 + x
            val worldY = sectionY * 16 + y
            val worldZ = chunk.z * 16 + z

            model.translate(worldX.toFloat(), worldY.toFloat(), worldZ.toFloat())
            blocks[index] = Block(
                this,
                worldX,
                worldY,
                worldZ,
                model,
                AABB.of(
                    Vector3f(worldX.toFloat(), worldY.toFloat(), worldZ.toFloat()),
                    Vector3f(worldX.toFloat() + 1f, worldY.toFloat() + 1f, worldZ.toFloat() + 1f)
                )
            )
        }
    }

    private fun getIndex(x: Int, y: Int, z: Int): Int {
        require(x in 0..15 && y in 0..15 && z in 0..15)
        return (y shl 8) or (z shl 4) or x
    }

    fun isEmpty(): Boolean = blocks.all { it == null }

    fun rebuildModel() {
        if(isEmpty()) {
            dispatcher?.cleanup()
            dispatcher = null
            return
        }

        dispatcher?.rebuild(blocks) ?: run {
           dispatcher = WorldModelDispatcher(
                chunk.world,
                VoxelGame.application.renderSystem.drawer,
                blocks,
                blocks.filterNotNull().first().model.texture.toAtlasTexture()
           )
        }
    }

    fun unload() {
        isLoaded = false
        dispatcher?.cleanup()
        dispatcher = null
    }

    fun load() {
        isLoaded = true
        rebuildModel()
    }

}
