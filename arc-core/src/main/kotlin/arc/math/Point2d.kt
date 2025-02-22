package arc.math

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * A point in a 2D grid, with integer x and y coordinates
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Point2d {

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

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        /**
         * Create new instance of [Point2d]
         *
         * @param x X position
         * @param y Y position
         *
         * @return New instance of [Point2d].
         */
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