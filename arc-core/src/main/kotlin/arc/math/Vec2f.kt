package arc.math

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.util.pattern.Interpolatable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Range
import kotlin.jvm.Throws

/**
 * This interface represents a 2D vector with float values.
 */
@ImmutableType
interface Vec2f : Vector<Vec2f>, Interpolatable<Vec2f> {

    /**
     * The X-coordinate of the 2D vector.
     */
    val x: Float

    /**
     * The Y-coordinate of the 2D vector.
     */
    val y: Float

    /**
     * Create new instance of [Vec2f] with new [x] and [y] value.
     *
     * This also creates a new instance if the current X or Y value equals new X or Y.
     *
     * @param x New X value.
     * @param y New Y value.
     *
     * @return New instance of [Vec2f].
     */
    @Contract("_, _ -> New")
    fun withXY(x: Float = this.x, y: Float = this.y): Vec2f = of(x, y)

    /**
     * Create new instance of [Vec2f] with new [x] value.
     *
     * This also creates a new instance if the current X value equals the new X.
     *
     * @param x New X value.
     *
     * @return New instance of [Vec2f].
     */
    @Contract("_ -> New")
    fun withX(x: Float): Vec2f = withXY(x = x)

    /**
     * Create new instance of [Vec2f] with new [y] value.
     *
     * This also creates a new instance if the current Y value equals new Y.
     *
     * @param y New Y value.
     *
     * @return New instance of [Vec2f].
     */
    @Contract("_ -> New")
    fun withY(y: Float): Vec2f = withXY(y = y)

    /**
     * Interpolate with another vector.
     *
     * @param other Vector for interpolation.
     * @param progress Progress of interpolation in ``0.0..1.0`` range.
     *
     * @return New instance of [Vec2f] with new values.
     *
     * @throws IllegalArgumentException If [progress] is not in ``0.0..1.0`` range.
     */
    @Throws(IllegalArgumentException::class)
    override fun interpolate(other: Vec2f, progress: @Range(from = 0, to = 1) Float): Vec2f

    operator fun component1(): Float = x
    operator fun component2(): Float = y

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(x: Float, y: Float): Vec2f

    }

    companion object {

        /**
         * [Vec2f] with zero values.
         */
        @JvmField
        val ZERO = of(0f, 0f)

        /**
         * Create instance of [Vec2f] by [Factory].
         *
         * @param x X position
         * @param y Y position.
         *
         * @return New instance of [Vec2f].
         */
        @JvmStatic
        fun of(x: Float, y: Float): Vec2f {
            return Arc.factory<Factory>().create(x, y)
        }

    }

}