package arc.demo.world.chunk

import arc.demo.world.World

class Chunk(
    val world: World,
    val x: Int,
    val z: Int
) {

    val sections = mutableListOf<ChunkSection>()

    fun updateAll() {
        sections.forEach {
            it.updateAll()
        }
    }

}