package arc.math

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents a 2D point with integer values.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Point2i {

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