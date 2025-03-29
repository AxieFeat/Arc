package arc.math

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents a 3D point with integer values.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Point3i {

    /**
     * The X-coordinate of the 3D point.
     */
    @get:JvmName("x")
    var x: Int

    /**
     * The Y-coordinate of the 3D point.
     */
    @get:JvmName("y")
    var y: Int

    /**
     * The Z-coordinate of the 3D point.
     */
    @get:JvmName("z")
    var z: Int

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