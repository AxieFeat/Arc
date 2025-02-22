package arc.graphics.g3d

import arc.annotations.ImmutableType
import arc.math.Point3i

/**
 * This interface represents chunk section.
 *
 * For convenience, you can combine several sections into one big one, like in Minecraft.
 * For example, in Minecraft a chunk is a lot of 16x16x16 sections.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface ChunkSection {

    /**
     * Offsets of this chunk in global position.
     */
    @get:JvmName("offset")
    val offset: Point3i

    /**
     * Set of all entities in this chunk.
     */
    @get:JvmName("entities")
    val entities: Set<Entity>

    /**
     * Is this section empty.
     */
    val isEmpty: Boolean

    /**
     * Is this section has changes from time of creation.
     */
    val isChanged: Boolean

    /**
     * Get entity by it ID.
     *
     * @param id ID of entity.
     *
     * @return Instance of [Entity] or null, if not found.
     */
    operator fun get(id: Int): Entity?

    /**
     * Add new entity to this section.
     *
     * @param entity Entity to add.
     *
     * @return [entity] for chaining.
     */
    fun add(entity: Entity): Entity

    /**
     * Update this chunk.
     */
    fun update()

}