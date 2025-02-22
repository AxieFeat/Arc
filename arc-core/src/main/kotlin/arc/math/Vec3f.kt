package arc.math

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a 3D vector with float components (x, y, z).
 * This class is mutable and allows modifications.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Vec3f : Vector<Vec3f> {

    /**
     * The X component of the 3D vector.
     *
     * Represents the horizontal axis value in 3D space. This value can be used or modified
     * as part of operations involving the vector.
     */
    @get:JvmName("x")
    val x: Float

    /**
     * The Y component of the 3D vector.
     *
     * Represents the vertical axis value in 3D space. This value can be used or modified
     * as part of operations involving the vector.
     */
    @get:JvmName("y")
    val y: Float

    /**
     * The Z component of the 3D vector.
     *
     * Represents the depth axis value in 3D space. This value is commonly used or modified
     * in operations involving 3D transformations or representations.
     */
    @get:JvmName("z")
    val z: Float

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        /**
         * Create new instance of [Vec3f]
         *
         * @param x X position
         * @param y Y position
         * @param z Z position
         *
         * @return New instance of [Vec3f].
         */
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