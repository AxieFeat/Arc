package arc.graphics.g3d

import arc.annotations.ImmutableType
import arc.math.Point3i

/**
 * This interface represents global chunk sections container.
 *
 * Note that this container stores an unlimited number of chunk sections,
 * they are created automatically when you try to retrieve one.
 */
@ImmutableType
interface ChunkContainer {

    /**
     * Get chunk section by it offset.
     *
     * @param point3i Point with offsets.
     *
     * @return Instance of [ChunkSection].
     */
    operator fun get(point3i: Point3i): ChunkSection

    /**
     * Get chunk section by it offset.
     *
     * @param x X offset of chunk.
     * @param y Y offset of chunk.
     * @param z Z offset of chunk.
     *
     * @return Instance of [ChunkSection].
     */
    operator fun get(x: Int, y: Int, z: Int): ChunkSection

    /**
     * Update chunk section on some location.
     *
     * @param point3i Point with offsets.
     */
    fun updateSection(point3i: Point3i)

    /**
     * Update chunk section on some location.
     *
     * @param x X offset of chunk.
     * @param y Y offset of chunk
     * @param z Z offset of chunk.
     */
    fun updateSection(x: Int, y: Int, z: Int)

}