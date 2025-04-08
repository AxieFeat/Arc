package arc.math

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.util.pattern.Copyable
import arc.util.pattern.Interpolatable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Range
import kotlin.jvm.Throws

/**
 * A point in a 2D grid, with integer x and y coordinates
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Point2d : Copyable<Point2d>, Interpolatable<Point2d> {

    /**
     * X position.
     */
    @get:JvmName("x")
    var x: Double

    /**
     * Y position.
     */
    @get:JvmName("y")
    var y: Double

    /**
     * Interpolate with other point.
     *
     * @param other Point for interpolation.
     * @param progress Progress of interpolation in ``0.0..1.0`` range.
     *
     * @return Current instance of [Point2d] with new values.
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