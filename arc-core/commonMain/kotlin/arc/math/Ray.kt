package arc.math

import arc.Arc.single
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.util.pattern.Copyable
import arc.annotations.InternalApi
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmField
import korlibs.math.geom.Vector3F

/**
 * Represents a 3D ray defined by an origin and a direction.
 */
@ImmutableType
interface Ray : Copyable<Ray> {

    /**
     * The origin point of the ray.
     */
    val origin: Vector3F

    /**
     * The direction vector of the ray.
     */
    val direction: Vector3F

    /**
     * Create new instance of [Ray] with new [origin] point.
     *
     * This also creates a new instance if the current origin point equals new.
     *
     * @param origin New origin point.
     *
     * @return New instance of [Ray].
     */
    fun withOrigin(origin: Vector3F): Ray = withOriginAndDirection(origin = origin)

    /**
     * Create new instance of [Ray] with new [direction] point.
     *
     * This also creates a new instance if the current direction point equals new.
     *
     * @param direction New direction point.
     *
     * @return New instance of [Ray].
     */
    fun withDirection(direction: Vector3F): Ray = withOriginAndDirection(direction = direction)

    /**
     * Create new instance of [Ray] with new [origin] and [direction] point.
     *
     * This also creates a new instance if the current origin or direction point equals new.
     *
     * @param origin New origin point.
     * @param direction New direction point.
     *
     * @return New instance of [Ray].
     */
    fun withOriginAndDirection(
        origin: Vector3F = this.origin,
        direction: Vector3F = this.direction
    ): Ray = of(origin, direction)

    /**
     * Returns the endpoint given the distance.
     *
     * @param distance The distance from the end point to the start point.
     *
     * @return Vector with result point.
     */
    fun getEndPoint(distance: Float): Vector3F

    /**
     * Create a copy of this ray.
     *
     * @return New instance of [Ray] with setting from current instance.
     */
    override fun copy(): Ray

    /**
     * Get [origin] component.
     */
    operator fun component1(): Vector3F = origin

    /**
     * Get [direction] component.
     */
    operator fun component2(): Vector3F = direction

    @InternalApi
    @TypeFactory
    interface Factory {

        fun create(origin: Vector3F, direction: Vector3F): Ray
    }

    companion object {

        /**
         * [Ray] with zero values.
         */
        @JvmField
        val ZERO = of(Vector3F(0f, 0f, 0f), Vector3F(0f, 0f, 0f))

        /**
         * Create new instance of [Ray].
         *
         * @param origin Origin of vector (Starting position).
         * @param direction Direction of vector.
         *
         * @return New instance of [Ray].
         */
        @JvmStatic
        fun of(origin: Vector3F, direction: Vector3F): Ray {
            return single<Factory>().create(origin, direction)
        }
    }
}
