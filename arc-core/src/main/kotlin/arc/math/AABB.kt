package arc.math

import arc.Arc
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents an axis-aligned bounding box (AABB).
 *
 * An AABB is defined by two 3D vectors: the minimum and maximum points.
 * These points describe the boundaries of the box in 3D space.
 */
interface AABB {

    /**
     * The minimum point of this axis-aligned bounding box.
     *
     * Represents the lower corner of the box in 3D space, defined by its smallest x, y, and z values.
     */
    var min: Vec3f

    /**
     * The maximum point of this axis-aligned bounding box.
     *
     * Represents the upper corner of the box in 3D space, defined by its largest x, y, and z values.
     */
    var max: Vec3f

    /**
     * The center point of this axis-aligned bounding box.
     */
    val center: Vec3f

    /**
     * Checks if a given point lies within this axis-aligned bounding box.
     *
     * @param point The point to check
     *
     * @return True if the point is inside this AABB, false otherwise.
     */
    fun contains(point: Vec3f): Boolean

    /**
     * Checks if another axis-aligned bounding box intersects with this one.
     *
     * @param other The other AABB to check for intersection.
     *
     * @return True if the two AABBs overlap, false otherwise.
     */
    fun intersects(other: AABB): Boolean

    operator fun component1(): Vec3f = min
    operator fun component2(): Vec3f = max
    operator fun component3(): Vec3f = center

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(min: Vec3f, max: Vec3f): AABB

    }

    companion object {

        /**
         * [AABB] with zero values.
         */
        @JvmField
        val ZERO = of(Vec3f.of(0f, 0f, 0f), Vec3f.of(0f, 0f, 0f))

        /**
         * Creates a new instance of an axis-aligned bounding box (AABB) using the given minimum and maximum points.
         *
         * @param min The minimum point of the bounding box, representing the lower corner in 3D space.
         * @param max The maximum point of the bounding box, representing the upper corner in 3D space.
         *
         * @return A new instance of [AABB] defined by the specified minimum and maximum points.
         */
        @JvmStatic
        fun of(min: Vec3f, max: Vec3f): AABB = Arc.factory<Factory>().create(min, max)

    }

}