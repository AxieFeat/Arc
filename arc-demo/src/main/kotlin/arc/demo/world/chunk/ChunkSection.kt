package arc.demo.world.chunk

import arc.demo.VoxelGame
import arc.demo.world.block.Block
import arc.graphics.ModelHandler
import arc.model.Face
import arc.model.Model
import arc.model.cube.Cube

class ChunkSection(
    val chunk: Chunk
) {

    var finalModel: Model? = null

    var handler: ModelHandler? = null

    val blocks = arrayOfNulls<Block?>(16 * 16 * 16)

    fun getBlock(x: Int, y: Int, z: Int): Block? {
        val index = getIndex(x, y, z)
        return blocks[index]
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

            model.elements.filterIsInstance<Cube>().forEach { cube ->
                cube.from.also {
                    it.x += worldX.toDouble()
                    it.y += worldY.toDouble()
                    it.z += worldZ.toDouble()
                }
                cube.to.also {
                    it.x += worldX.toDouble()
                    it.y += worldY.toDouble()
                    it.z += worldZ.toDouble()
                }
            }

            blocks[index] = Block(this, worldX, worldY, worldZ, model)
        }
    }

    private fun getIndex(x: Int, y: Int, z: Int): Int {
        require(x in 0..15 && y in 0..15 && z in 0..15) { "Coordinates out of bounds" }
        return (y shl 8) or (z shl 4) or x
    }

    fun update() {
        var first = blocks.first()?.model ?: return

        for(i in 1..<blocks.size) {
            blocks[i]?.model?.let { first = first.merge(it) }
        }

        finalModel = first

        finalModel?.let { model ->
            model.elements.filterIsInstance<Cube>().forEach { cube ->
                cube.faces.toMutableMap().forEach { face ->
                    when(face.key) {
                        Face.NORTH -> {
                            if(getBlock(cube.from.x.toInt() + 1, cube.from.y.toInt(), cube.from.z.toInt()) != null) {
                                cube.faces.remove(face.key)
                            }
                        }
                        Face.EAST -> {

                        }
                        Face.SOUTH -> {

                        }
                        Face.WEST -> {

                        }
                        Face.UP -> {
                            if(getBlock(cube.from.x.toInt(), cube.from.y.toInt() + 1, cube.from.z.toInt()) != null) {
                                cube.faces.remove(face.key)
                            }
                        }
                        Face.DOWN -> {
                            if(getBlock(cube.from.x.toInt(), cube.from.y.toInt() - 1, cube.from.z.toInt()) != null) {
                                cube.faces.remove(face.key)
                            }
                        }
                    }
                }
            }
        }

        handler = ModelHandler.of(VoxelGame.application.renderSystem.drawer, finalModel!!)
    }
}