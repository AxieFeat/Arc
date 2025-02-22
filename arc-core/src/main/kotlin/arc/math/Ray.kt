package arc.math

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.util.Copyable
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a 3D ray defined by an origin and a direction.
 *
 * A ray is a mathematical construct commonly used in graphics and physics calculations
 * to represent an infinite line, starting from an origin point and extending in
 * the specified direction.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Ray : Copyable<Ray> {

    /**
     * The origin point of the ray in 3D space.
     *
     * Represents the starting position of the ray, defined as a 3D vector.
     * This property can be modified to update the ray's origin.
     */
    @get:JvmName("origin")
    var origin: Vec3f

    /**
     * The direction vector of the ray in 3D space.
     *
     * Represents the orientation of the ray, typically used in conjunction with the origin
     * to determine the ray's path. This can be altered to change the direction of the ray.
     */
    @get:JvmName("direction")
    var direction: Vec3f

    /**
     * Returns the endpoint given the distance.
     *
     * @param out The vector to set to the result.
     * @param distance The distance from the end point to the start point.
     *
     * @return The [out] param.
     */
    fun getEndPoint(out: Vec3f, distance: Float): Vec3f

    /**
     * Create copy of this ray.
     *
     * @return New instance of [Ray] with setting from current instance.
     */
    override fun copy(): Ray

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create new instance of [Ray].
         *
         * @param origin Origin of vector (Starting position).
         * @param direction Direction of vector.
         *
         * @return New instance of [Ray].
         */
        fun create(origin: Vec3f, direction: Vec3f): Ray

    }

    companion object {

        /**
         * [Ray] with zero values.
         */
        @JvmField
        val ZERO = of(Vec3f.ZERO, Vec3f.ZERO)

        /**
         * Create new instance of [Ray].
         *
         * @param origin Origin of vector (Starting position).
         * @param direction Direction of vector.
         *
         * @return New instance of [Ray].
         */
        @JvmStatic
        fun of(origin: Vec3f, direction: Vec3f): Ray {
            return Arc.factory<Factory>().create(origin, direction)
        }

    }

}