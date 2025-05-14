package arc.math

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.util.pattern.Copyable
import arc.util.pattern.Interpolatable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Range
import kotlin.jvm.Throws

/**
 * A point in a 2D grid, with integer x and y coordinates
 */
@ImmutableType
interface Point2d : Copyable<Point2d>, Interpolatable<Point2d> {

    /**
     * X position.
     */
    val x: Double

    /**
     * Y position.
     */
    val y: Double

    /**
     * Create new instance of [Point2d] with new [x] and [y] value.
     *
     * This also create new instance if current X or Y value equals new X or Y.
     *
     * @param x New X value.
     * @param y New Y value.
     *
     * @return New instance of [Point2d].
     */
    @Contract("_, _ -> New")
    fun withXY(x: Double = this.x, y: Double = this.y): Point2d = of(x, y)

    /**
     * Create new instance of [Point2d] with new [x] value.
     *
     * This also create new instance if current X value equals new X.
     *
     * @param x New X value.
     *
     * @return New instance of [Point2d].
     */
    @Contract("_ -> New")
    fun withX(x: Double): Point2d = withXY(x = x)

    /**
     * Create new instance of [Point2d] with new [y] value.
     *
     * This also create new instance if current Y value equals new Y.
     *
     * @param y New Y value.
     *
     * @return New instance of [Point2d].
     */
    @Contract("_ -> New")
    fun withY(y: Double): Point2d = withXY(y = y)

    /**
     * Interpolate with other point.
     *
     * @param other Point for interpolation.
     * @param progress Progress of interpolation in ``0.0..1.0`` range.
     *
     * @return New instance of [Point2d] with new values.
     *
     * @throws IllegalArgumentException If [progress] is not in ``0.0..1.0`` range.
     */
    @Throws(IllegalArgumentException::class)
    override fun interpolate(other: Point2d, progress: @Range(from = 0, to = 1) Float): Point2d

    operator fun component1(): Double = x
    operator fun component2(): Double = y

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(x: Double, y: Double): Point2d

    }

    companion object {

        /**
         * [Point2d] with zero values.
         */
        @JvmField
        val ZERO = of(0.0, 0.0)

        /**
         * Create instance of [Point2d] by [Factory].
         *
         * @param x X position
         * @param y Y position.
         *
         * @return New instance of [Point2d].
         */
        @JvmStatic
        fun of(x: Double, y: Double): Point2d {
            return Arc.factory<Factory>().create(x, y)
        }
    }
}