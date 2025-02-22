package arc.math

import arc.annotations.ImmutableType

/**
 * Represents an axis-aligned bounding box (AABB).
 *
 * An AABB is defined by two 3D vectors: the minimum and maximum points.
 * These points describe the boundaries of the box in 3D space.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface AABB {

    /**
     * The minimum point of this axis-aligned bounding box.
     *
     * Represents the lower corner of the box in 3D space, defined by its smallest x, y, and z values.
     */
    @get:JvmName("min")
    val min: Vec3f

    /**
     * The maximum point of this axis-aligned bounding box.
     *
     * Represents the upper corner of the box in 3D space, defined by its largest x, y, and z values.
     */
    @get:JvmName("max")
    val max: Vec3f

    /**
     * The center point of this axis-aligned bounding box.
     */
    @get:JvmName("center")
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

}