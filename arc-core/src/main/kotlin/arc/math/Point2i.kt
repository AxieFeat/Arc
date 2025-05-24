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
 * This interface represents a 2D point with integer values.
 */
@ImmutableType
interface Point2i : Copyable<Point2i>, Interpolatable<Point2i> {

    /**
     * The X-coordinate of the 2D point.
     */
    val x: Int

    /**
     * The Y-coordinate of the 2D point.
     */
    val y: Int

    /**
     * Create new instance of [Point2i] with new [x] and [y] value.
     *
     * This also creates a new instance if the current X or Y value equals new X or Y.
     *
     * @param x New X value.
     * @param y New Y value.
     *
     * @return New instance of [Point2i].
     */
    @Contract("_, _ -> New")
    fun withXY(x: Int = this.x, y: Int = this.y): Point2i = of(x, y)

    /**
     * Create new instance of [Point2i] with new [x] value.
     *
     * This also creates a new instance if the current X value equals the new X.
     *
     * @param x New X value.
     *
     * @return New instance of [Point2i].
     */
    @Contract("_ -> New")
    fun withX(x: Int): Point2i = withXY(x = x)

    /**
     * Create new instance of [Point2i] with new [y] value.
     *
     * This also creates a new instance if the current Y value equals new Y.
     *
     * @param y New Y value.
     *
     * @return New instance of [Point2i].
     */
    @Contract("_ -> New")
    fun withY(y: Int): Point2i = withXY(y = y)

    /**
     * Interpolate with another point.
     *
     * @param other Point for interpolation.
     * @param progress Progress of interpolation in ``0.0..1.0`` range.
     *
     * @return New instance of [Point2i] with new values.
     *
     * @throws IllegalArgumentException If [progress] is not in ``0.0..1.0`` range.
     */
    @Throws(IllegalArgumentException::class)
    override fun interpolate(other: Point2i, progress: @Range(from = 0, to = 1) Float): Point2i

    operator fun component1(): Int = x
    operator fun component2(): Int = y

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(x: Int, y: Int): Point2i

    }

    companion object {

        /**
         * [Point2i] with zero values.
         */
        @JvmField
        val ZERO = of(0, 0)

        /**
         * Create instance of [Point2i] by [Factory].
         *
         * @param x X position
         * @param y Y position.
         *
         * @return New instance of [Point2i].
         */
        @JvmStatic
        fun of(x: Int, y: Int): Point2i {
            return Arc.factory<Factory>().create(x, y)
        }

    }

}