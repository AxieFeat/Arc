package arc.math

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.util.pattern.Copyable
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a 3D ray defined by an origin and a direction.
 */
@MutableType
interface Ray : Copyable<Ray> {

    /**
     * The origin point of the ray.
     */
    var origin: Vec3f

    /**
     * The direction vector of the ray.
     */
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

    operator fun component1(): Vec3f = origin
    operator fun component2(): Vec3f = direction

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(origin: Vec3f, direction: Vec3f): Ray

    }

    companion object {

        /**
         * [Ray] with zero values.
         */
        @JvmField
        val ZERO = of(Vec3f.of(0f, 0f, 0f), Vec3f.of(0f, 0f, 0f))

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