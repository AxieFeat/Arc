package arc.math

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.util.pattern.Interpolatable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Range

/**
 * Represents a 3D vector with float values.
 */
@ImmutableType
interface Vec3f : Vector<Vec3f>, Interpolatable<Vec3f> {

    /**
     * The X-coordinate of the 3D vector.
     */
    val x: Float

    /**
     * The Y-coordinate of the 3D vector.
     */
    val y: Float

    /**
     * The Z-coordinate of the 3D vector.
     */
    val z: Float

    /**
     * Create new instance of [Vec3f] with new [x], [y] and [z] value.
     *
     * This also creates a new instance if the current X or Y or Z value equals new X or Y or Z.
     *
     * @param x New X value.
     * @param y New Y value.
     * @param z New Z Value.
     *
     * @return New instance of [Vec3f].
     */
    @Contract("_, _, _ -> New")
    fun withXYZ(x: Float = this.x, y: Float = this.y, z: Float = this.z): Vec3f = of(x, y, z)

    /**
     * Create new instance of [Vec3f] with new [x] value.
     *
     * This also creates a new instance if the current X value equals the new X.
     *
     * @param x New X value.
     *
     * @return New instance of [Vec3f].
     */
    @Contract("_ -> New")
    fun withX(x: Float): Vec3f = withXYZ(x = x)

    /**
     * Create new instance of [Vec3f] with new [y] value.
     *
     * This also creates a new instance if the current Y value equals new Y.
     *
     * @param y New Y value.
     *
     * @return New instance of [Vec3f].
     */
    @Contract("_ -> New")
    fun withY(y: Float): Vec3f = withXYZ(y = y)

    /**
     * Create new instance of [Vec3f] with new [z] value.
     *
     * This also creates a new instance if the current Z value equals new Z.
     *
     * @param z New Z value.
     *
     * @return New instance of [Vec3f].
     */
    @Contract("_ -> New")
    fun withZ(z: Float): Vec3f = withXYZ(z = z)

    /**
     * Interpolate with another vector.
     *
     * @param other Vector for interpolation.
     * @param progress Progress of interpolation in ``0.0..1.0`` range.
     *
     * @return New instance of [Vec3f] with new values.
     *
     * @throws IllegalArgumentException If [progress] is not in ``0.0..1.0`` range.
     */
    @Throws(IllegalArgumentException::class)
    override fun interpolate(other: Vec3f, progress: @Range(from = 0, to = 1) Float): Vec3f

    operator fun component1(): Float = x
    operator fun component2(): Float = y
    operator fun component3(): Float = z

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(x: Float, y: Float, z: Float): Vec3f

    }

    companion object {

        /**
         * [Vec3f] with zero values.
         */
        @JvmField
        val ZERO = of(0f, 0f, 0f)

        /**
         * Create instance of [Vec3f] by [Factory].
         *
         * @param x X position
         * @param y Y position.
         * @param z Z position.
         *
         * @return New instance of [Vec3f].
         */
        @JvmStatic
        fun of(x: Float, y: Float, z: Float): Vec3f {
            return Arc.factory<Factory>().create(x, y, z)
        }

    }

}