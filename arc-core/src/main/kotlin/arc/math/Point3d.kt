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
 * A point in a 3D grid, with integer x, y and z coordinates
 */
@MutableType
interface Point3d : Copyable<Point3d>, Interpolatable<Point3d> {

    /**
     * X position.
     */
    var x: Double

    /**
     * Y position.
     */
    var y: Double

    /**
     * Z position.
     */
    var z: Double

    /**
     * Interpolate with other point.
     *
     * @param other Point for interpolation.
     * @param progress Progress of interpolation in ``0.0..1.0`` range.
     *
     * @return Current instance of [Point3d] with new values.
     *
     * @throws IllegalArgumentException If [progress] is not in ``0.0..1.0`` range.
     */
    @Throws(IllegalArgumentException::class)
    override fun interpolate(other: Point3d, progress: @Range(from = 0, to = 1) Float): Point3d

    operator fun component1(): Double = x
    operator fun component2(): Double = y
    operator fun component3(): Double = z

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(x: Double, y: Double, z: Double): Point3d

    }

    companion object {

        /**
         * [Point3d] with zero values.
         */
        @JvmField
        val ZERO = of(0.0, 0.0, 0.0)

        /**
         * Create instance of [Point3d] by [Factory].
         *
         * @param x X position
         * @param y Y position.
         * @param z Z position.
         *
         * @return New instance of [Point3d].
         */
        @JvmStatic
        fun of(x: Double, y: Double, z: Double): Point3d {
            return Arc.factory<Factory>().create(x, y, z)
        }

    }

}