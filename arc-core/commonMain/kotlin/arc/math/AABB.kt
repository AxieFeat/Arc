package arc.math

import arc.Arc.single
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.annotations.InternalApi
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads
import korlibs.math.geom.Vector3F

/**
 * Represents an axis-aligned bounding box (AABB).
 *
 * An AABB is defined by two 3D vectors: the minimum and maximum points.
 * These points describe the boundaries of the box in 3D space.
 */
@ImmutableType
interface AABB {

    /**
     * The minimum point of this axis-aligned bounding box.
     *
     * Represents the lower corner of the box in 3D space, defined by its smallest x, y, and z values.
     */
    val min: Vector3F

    /**
     * The maximum point of this axis-aligned bounding box.
     *
     * Represents the upper corner of the box in 3D space, defined by its largest x, y, and z values.
     */
    val max: Vector3F

    /**
     * The center point of this axis-aligned bounding box.
     */
    val center: Vector3F

    /**
     * Create new instance of [AABB] with new [min], [max] and [center] points.
     *
     * This also create new instance if current min or max or center points equals new.
     *
     * @param min New min point.
     * @param max New max point.
     * @param center New center point.
     *
     * @return New instance of [AABB].
     */
    fun withMinMaxCenter(
        min: Vector3F = this.min,
        max: Vector3F = this.max,
        center: Vector3F = this.center,
    ): AABB = of(min, max, center)

    /**
     * Create new instance of [AABB] with new [min] point.
     *
     * This also creates a new instance if the current min point equals new.
     *
     * @param min New min point.
     *
     * @return New instance of [AABB].
     */
    fun withMin(min: Vector3F): AABB = withMinMaxCenter(min = min)

    /**
     * Create new instance of [AABB] with new [max] point.
     *
     * This also creates a new instance if the current max point equals new.
     *
     * @param max New max point.
     *
     * @return New instance of [AABB].
     */
    fun withMax(max: Vector3F): AABB = withMinMaxCenter(max = max)

    /**
     * Create new instance of [AABB] with new [center] point.
     *
     * This also creates a new instance if the current center point equals new.
     *
     * @param center New center point.
     *
     * @return New instance of [AABB].
     */
    fun withCenter(center: Vector3F): AABB = withMinMaxCenter(center = center)

    /**
     * Checks if a given point lies within this axis-aligned bounding box.
     *
     * @param point The point to check
     *
     * @return True if the point is inside this AABB, false otherwise.
     */
    fun contains(point: Vector3F): Boolean

    /**
     * Checks if another axis-aligned bounding box intersects with this one.
     *
     * @param other The other AABB to check for intersection.
     *
     * @return True if the two AABBs overlap, false otherwise.
     */
    fun intersects(other: AABB): Boolean

    /**
     * Get [min] component.
     */
    operator fun component1(): Vector3F = min

    /**
     * Get [max] component.
     */
    operator fun component2(): Vector3F = max

    /**
     * Get [center] component.
     */
    operator fun component3(): Vector3F = center

    @InternalApi
    @TypeFactory
    interface Factory {

        fun create(min: Vector3F, max: Vector3F, center: Vector3F): AABB
    }

    companion object {

        /**
         * [AABB] with zero values.
         */
        @JvmField
        val ZERO = of(Vector3F(0f, 0f, 0f), Vector3F(0f, 0f, 0f))

        /**
         * Creates a new instance of an axis-aligned bounding box (AABB) using the given minimum and maximum points.
         *
         * @param min The minimum point of the bounding box, representing the lower corner in 3D space.
         * @param max The maximum point of the bounding box, representing the upper corner in 3D space.
         * @param center The center point of the bounding box.
         *
         * @return A new instance of [AABB] defined by the specified minimum and maximum points.
         */
        @JvmOverloads
        @JvmStatic
        fun of(
            min: Vector3F,
            max: Vector3F,
            center: Vector3F = Vector3F(
                (min.x + max.x) * 0.5f,
                (min.y + max.y) * 0.5f,
                (min.z + max.z) * 0.5f
            )
        ): AABB = single<Factory>().create(min, max, center)
    }
}
