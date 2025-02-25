package arc.graphics

import arc.graphics.g3d.ChunkContainer
import arc.graphics.g3d.ChunkSection
import arc.math.Point3i

internal class ArcChunkContainer : ChunkContainer {

    private val chunks = mutableSetOf<ChunkSection>()

    override fun get(point3i: Point3i): ChunkSection {
        return chunks.find { it.offset == point3i } ?: run {
            val newChunk = ArcChunkSection(point3i)

            chunks.add(newChunk)

            newChunk
        }
    }

    override fun get(x: Int, y: Int, z: Int): ChunkSection {
        return get(Point3i.of(x, y, z))
    }
}