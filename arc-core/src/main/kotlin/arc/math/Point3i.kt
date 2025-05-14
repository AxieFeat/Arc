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
 * This interface represents a 3D point with integer values.
 */
@ImmutableType
interface Point3i : Copyable<Point3i>, Interpolatable<Point3i> {

    /**
     * The X-coordinate of the 3D point.
     */
    val x: Int

    /**
     * The Y-coordinate of the 3D point.
     */
    val y: Int

    /**
     * The Z-coordinate of the 3D point.
     */
    val z: Int

    /**
     * Create new instance of [Point3i] with new [x], [y] and [z] value.
     *
     * This also create new instance if current X or Y or Z value equals new X or Y or Z.
     *
     * @param x New X value.
     * @param y New Y value.
     * @param z New Z Value.
     *
     * @return New instance of [Point3i].
     */
    @Contract("_, _, _ -> New")
    fun withXYZ(x: Int = this.x, y: Int = this.y, z: Int = this.z): Point3i = of(x, y, z)

    /**
     * Create new instance of [Point3i] with new [x] value.
     *
     * This also create new instance if current X value equals new X.
     *
     * @param x New X value.
     *
     * @return New instance of [Point3i].
     */
    @Contract("_ -> New")
    fun withX(x: Int): Point3i = withXYZ(x = x)

    /**
     * Create new instance of [Point3i] with new [y] value.
     *
     * This also create new instance if current Y value equals new Y.
     *
     * @param y New Y value.
     *
     * @return New instance of [Point3i].
     */
    @Contract("_ -> New")
    fun withY(y: Int): Point3i = withXYZ(y = y)

    /**
     * Create new instance of [Point3i] with new [z] value.
     *
     * This also create new instance if current Z value equals new Z.
     *
     * @param z New Z value.
     *
     * @return New instance of [Point3i].
     */
    @Contract("_ -> New")
    fun withZ(z: Int): Point3i = withXYZ(z = z)

    /**
     * Interpolate with other point.
     *
     * @param other Point for interpolation.
     * @param progress Progress of interpolation in ``0.0..1.0`` range.
     *
     * @return New instance of [Point3i] with new values.
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