package arc.math

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.util.Copyable
import arc.util.Interpolatable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Range
import kotlin.jvm.Throws

/**
 * This interface represents a 3D point with integer values.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Point3i : Copyable<Point3i>, Interpolatable<Point3i> {

    /**
     * The X-coordinate of the 3D point.
     */
    @get:JvmName("x")
    var x: Int

    /**
     * The Y-coordinate of the 3D point.
     */
    @get:JvmName("y")
    var y: Int

    /**
     * The Z-coordinate of the 3D point.
     */
    @get:JvmName("z")
    var z: Int

    /**
     * Interpolate with other point.
     *
     * @param other Point for interpolation.
     * @param progress Progress of interpolation in ``0.0..1.0`` range.
     *
     * @return Current instance of [Point3i] with new values.
     *
     * @throws IllegalArgumentException If [progress] is not in ``0.0..1.0`` range.
     */
    @Throws(IllegalArgumentException::class)
    override fun interpolate(other: Point3i, progress: @Range(from = 0, to = 1) Float): Point3i

    operator fun component1(): Int = x
    operator fun component2(): Int = y
    operator fun component3(): Int = z

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(x: Int, y: Int, z: Int): Point3i

    }

    companion object {

        /**
         * [Point3i] with zero values.
         */
        @JvmField
        val ZERO = of(0, 0, 0)

        /**
         * Create instance of [Point3i] by [Factory].
         *
         * @param x X position
         * @param y Y position.
         * @param z Z position.
         *
         * @return New instance of [Point3i].
         */
        @JvmStatic
        fun of(x: Int, y: Int, z: Int): Point3i {
            return Arc.factory<Factory>().create(x, y, z)
        }

    }

}