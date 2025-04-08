package arc.math

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.util.pattern.Interpolatable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Range
import kotlin.jvm.Throws

/**
 * This interface represents a 2D vector with float values.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Vec2f : Vector<Vec2f>, Interpolatable<Vec2f> {

    /**
     * The X-coordinate of the 2D vector.
     */
    @get:JvmName("x")
    var x: Float

    /**
     * The Y-coordinate of the 2D vector.
     */
    @get:JvmName("y")
    var y: Float

    /**
     * Interpolate with other vector.
     *
     * @param other Vector for interpolation.
     * @param progress Progress of interpolation in ``0.0..1.0`` range.
     *
     * @return Current instance of [Vec2f] with new values.
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