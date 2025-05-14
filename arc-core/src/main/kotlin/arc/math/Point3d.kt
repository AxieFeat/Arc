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
 * A point in a 3D grid, with integer x, y and z coordinates
 */
@ImmutableType
interface Point3d : Copyable<Point3d>, Interpolatable<Point3d> {

    /**
     * X position.
     */
    val x: Double

    /**
     * Y position.
     */
    val y: Double

    /**
     * Z position.
     */
    val z: Double

    /**
     * Create new instance of [Point3d] with new [x], [y] and [z] value.
     *
     * This also create new instance if current X or Y or Z value equals new X or Y or Z.
     *
     * @param x New X value.
     * @param y New Y value.
     * @param z New Z Value.
     *
     * @return New instance of [Point3d].
     */
    @Contract("_, _, _ -> New")
    fun withXYZ(x: Double = this.x, y: Double = this.y, z: Double = this.z): Point3d = of(x, y, z)

    /**
     * Create new instance of [Point3d] with new [x] value.
     *
     * This also create new instance if current X value equals new X.
     *
     * @param x New X value.
     *
     * @return New instance of [Point3d].
     */
    @Contract("_ -> New")
    fun withX(x: Double): Point3d = withXYZ(x = x)

    /**
     * Create new instance of [Point3d] with new [y] value.
     *
     * This also create new instance if current Y value equals new Y.
     *
     * @param y New Y value.
     *
     * @return New instance of [Point3d].
     */
    @Contract("_ -> New")
    fun withY(y: Double): Point3d = withXYZ(y = y)

    /**
     * Create new instance of [Point3d] with new [z] value.
     *
     * This also create new instance if current Z value equals new Z.
     *
     * @param z New Z value.
     *
     * @return New instance of [Point3d].
     */
    @Contract("_ -> New")
    fun withZ(z: Double): Point3d = withXYZ(z = z)

    /**
     * Interpolate with other point.
     *
     * @param other Point for interpolation.
     * @param progress Progress of interpolation in ``0.0..1.0`` range.
     *
     * @return New instance of [Point3d] with new values.
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