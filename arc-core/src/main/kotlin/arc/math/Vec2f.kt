package arc.math

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents a 2D vector with float values.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Vec2f : Vector<Vec2f> {

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