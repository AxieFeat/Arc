package arc.demo.world

import arc.graphics.g3d.ChunkSection
import arc.graphics.g3d.Entity

data class Chunk(
    override val xOffset: Int,
    override val zOffset: Int
) : ChunkSection {

    override val entities: MutableSet<Entity> = mutableSetOf()

    override val isEmpty: Boolean
        get() = entities.isEmpty()

    override fun get(id: Int): Entity? {
        return entities.find { it.id == id }
    }

    override fun add(entity: Entity): Entity {
        entities.add(entity)

        return entity
    }

    override fun update() {
        // Nothing
    }

    override fun toString(): String {
        return "[$xOffset:$zOffset]"
    }

}