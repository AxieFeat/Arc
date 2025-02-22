package arc.math

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a 2D point with integer coordinates.
 *
 * This interface defines a mutable type for storing and manipulating
 * a point in a 2D space using `x` and `y` integer coordinates.
 * The point can be packed into a single long value for efficient storage
 * or transmission and can be unpacked back into its individual components.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Point2i {

    /**
     * The X coordinate of the 2D point.
     *
     * Represents the horizontal position of the point in a 2D space. This property can be
     * accessed or modified to change the X coordinate of the point.
     */
    @get:JvmName("x")
    var x: Int

    /**
     * The Y coordinate of the 2D point.
     *
     * Represents the vertical axis value of this point. This property can be used to get or modify
     * the Y position in a 2D space.
     */
    @get:JvmName("y")
    var y: Int

    /**
     * Packs the x and y integer fields into a single long value.
     * The x value occupies the higher 32 bits, while the y value occupies the lower 32 bits.
     *
     * @return A long value combining the x and y values.
     */
    fun pack(): Long {
        return (x.toLong() shl 32) or ((y.toLong() and 0xFFFFFFFFL))
    }

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        /**
         * Create new instance of [Point2i]
         *
         * @param x X position
         * @param y Y position
         *
         * @return New instance of [Point2i].
         */
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

        /**
         * Unpack point.
         *
         * @param packed Packet point via [Point2i.pack].
         *
         * @return Unpacked instance of [Point2i].
         */
        @JvmStatic
        fun unpack(packed: Long): Point2i {
            return of(
                (packed shr 32).toInt(),
                (packed and 0xFFFFFFFFL).toInt()
            )
        }

    }

}