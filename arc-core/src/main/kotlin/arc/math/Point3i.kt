package arc.math

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a 3D point with integer precision.
 *
 * This interface defines a mutable 3-dimensional point (x, y, z) where each component
 * is an integer value. It provides methods for packing the point into a single
 * integer and for creating and unpacking instances of [Point3i].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Point3i {

    /**
     * The X-coordinate of the 3D point.
     *
     * Represents the horizontal axis value of the point within a 3D space. This property
     * is mutable and can be changed to update the X-coordinate of the point.
     */
    @get:JvmName("x")
    var x: Int

    /**
     * Represents the y-coordinate of a 3D integer point.
     *
     * The y variable is a mutable property used to define the vertical axis value
     * of a point in 3D integer space. It is part of a coordinate system where it
     * typically works alongside x and z to determine the position of the point.
     */
    @get:JvmName("y")
    var y: Int

    /**
     * The Z component of a 3D integer point.
     *
     * Represents the depth coordinate in a 3D space, corresponding to the third dimension.
     * This property can be accessed or modified to represent the Z-axis value of the point.
     */
    @get:JvmName("z")
    var z: Int

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        /**
         * Create new instance of [Point3i]
         *
         * @param x X position
         * @param y Y position
         * @param z Z position
         *
         * @return New instance of [Point3i].
         */
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