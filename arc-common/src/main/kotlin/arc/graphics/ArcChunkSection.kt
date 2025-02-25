package arc.graphics

import arc.graphics.g3d.ChunkSection
import arc.graphics.g3d.Entity
import arc.math.Point3i
import java.util.UUID

internal data class ArcChunkSection(
    override val offset: Point3i,
    override val entities: MutableSet<Entity> = mutableSetOf()
) : ChunkSection {

    override val isEmpty: Boolean
        get() = entities.isEmpty()

    override var isChanged: Boolean = false

    override fun get(uuid: UUID): Entity? {
        return entities.find { it.uuid == uuid }
    }

    override fun add(entity: Entity): Entity {
        isChanged = true
        entities.add(entity)

        return entity
    }
}