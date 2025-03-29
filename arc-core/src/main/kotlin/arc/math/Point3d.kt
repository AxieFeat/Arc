package arc.math

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * A point in a 3D grid, with integer x, y and z coordinates
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Point3d {

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

    /**
     * Z position.
     */
    @get:JvmName("z")
    var z: Double

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(x: Double, y: Double, z: Double): Point3d

    }

    companion object {

        /**
         * [Point3d] with zero values.
         */
        @JvmField
        val ZERO = of(0.0, 0.0, 0.0)

        /**
         * Create instance of [Point3d] by [Factory].
         *
         * @param x X position
         * @param y Y position.
         * @param z Z position.
         *
         * @return New instance of [Point3d].
         */
        @JvmStatic
        fun of(x: Double, y: Double, z: Double): Point3d {
            return Arc.factory<Factory>().create(x, y, z)
        }

    }

}