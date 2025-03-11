package arc.math

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a 2D vector with floating-point components.
 * This class is mutable and allows modifications.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Vec2f : Vector<Vec2f> {

    /**
     * The X component of the 2D vector.
     *
     * Represents the horizontal axis value in 2D space. This value can be accessed or modified
     * as part of operations involving the vector.
     */
    @get:JvmName("x")
    var x: Float

    /**
     * The Y component of the 2D vector.
     *
     * Represents the vertical axis value in 2D space. This value can be used or modified
     * as part of operations involving the vector.
     */
    @get:JvmName("y")
    var y: Float

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        /**
         * Create new instance of [Vec2f]
         *
         * @param x X position
         * @param y Y position
         *
         * @return New instance of [Vec2f].
         */
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