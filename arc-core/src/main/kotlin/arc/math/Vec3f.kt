package arc.math

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a 3D vector with float values.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Vec3f : Vector<Vec3f> {

    /**
     * The X-coordinate of the 3D vector.
     */
    @get:JvmName("x")
    var x: Float

    /**
     * The Y-coordinate of the 3D vector.
     */
    @get:JvmName("y")
    var y: Float

    /**
     * The Z-coordinate of the 3D vector.
     */
    @get:JvmName("z")
    var z: Float

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