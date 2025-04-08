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
 * This interface represents a 2D point with integer values.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Point2i : Copyable<Point2i>, Interpolatable<Point2i> {

    /**
     * The X-coordinate of the 2D point.
     */
    @get:JvmName("x")
    var x: Int

    /**
     * The Y-coordinate of the 2D point.
     */
    @get:JvmName("y")
    var y: Int

    /**
     * Interpolate with other point.
     *
     * @param other Point for interpolation.
     * @param progress Progress of interpolation in ``0.0..1.0`` range.
     *
     * @return Current instance of [Point2i] with new values.
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