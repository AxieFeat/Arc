package arc.math

import arc.Arc.single
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.util.pattern.Copyable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.joml.Vector3f

/**
 * Represents a 3D ray defined by an origin and a direction.
 */
@ImmutableType
interface Ray : Copyable<Ray> {

    /**
     * The origin point of the ray.
     */
    val origin: Vector3f

    /**
     * The direction vector of the ray.
     */
    val direction: Vector3f

    /**
     * Create new instance of [Ray] with new [origin] point.
     *
     * This also creates a new instance if the current origin point equals new.
     *
     * @param origin New origin point.
     *
     * @return New instance of [Ray].
     */
    @Contract("_ -> New")
    fun withOrigin(origin: Vector3f): Ray = withOriginAndDirection(origin = origin)

    /**
     * Create new instance of [Ray] with new [direction] point.
     *
     * This also creates a new instance if the current direction point equals new.
     *
     * @param direction New direction point.
     *
     * @return New instance of [Ray].
     */
    @Contract("_ -> New")
    fun withDirection(direction: Vector3f): Ray = withOriginAndDirection(direction = direction)

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
    @Contract("_, _ -> New")
    fun withOriginAndDirection(
        origin: Vector3f = this.origin,
        direction: Vector3f = this.direction
    ): Ray = of(origin, direction)

    /**
     * Returns the endpoint given the distance.
     *
     * @param distance The distance from the end point to the start point.
     *
     * @return Vector with result point.
     */
    fun getEndPoint(distance: Float): Vector3f

    /**
     * Create a copy of this ray.
     *
     * @return New instance of [Ray] with setting from current instance.
     */
    override fun copy(): Ray

    operator fun component1(): Vector3f = origin
    operator fun component2(): Vector3f = direction

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(origin: Vector3f, direction: Vector3f): Ray

    }

    companion object {

        /**
         * [Ray] with zero values.
         */
        @JvmField
        val ZERO = of(Vector3f(), Vector3f())

        /**
         * Create new instance of [Ray].
         *
         * @param origin Origin of vector (Starting position).
         * @param direction Direction of vector.
         *
         * @return New instance of [Ray].
         */
        @JvmStatic
        fun of(origin: Vector3f, direction: Vector3f): Ray {
            return single<Factory>().create(origin, direction)
        }

    }

}