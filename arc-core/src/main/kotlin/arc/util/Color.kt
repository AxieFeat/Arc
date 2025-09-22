package arc.util

import arc.Arc.single
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.util.pattern.Copyable
import arc.util.pattern.Interpolatable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Range
import kotlin.jvm.Throws

/**
 * Represents a mutable color, which includes red, green, blue, and alpha (transparency) channels.
 * Each channel must adhere to specific ranges:
 * - Red, green, and blue values are integers between 0 and 255 (inclusive).
 * - Alpha value is a double between 0.0 and 1.0 (inclusive), where 1.0 represents full opacity and 0.0 full transparency.
 */
@ImmutableType
interface Color : Copyable<Color>, Interpolatable<Color> {

    /**
     * Represents the red component of a color.
     *
     * This value must be in the range ``0..255``.
     */
    val red: @Range(from = 0, to = 255) Int

    /**
     * Represents the green component of a color.
     *
     * This value must be in the range ``0..255``.
     */
    val green: @Range(from = 0, to = 255) Int

    /**
     * Represents the blue component of a color.
     *
     * This value must be in the range ``0..255``.
     */
    val blue: @Range(from = 0, to = 255) Int

    /**
     * Represents the alpha (opacity) component of a color.
     *
     * The value must be double within the range of 0.0 to 1.0, where 0.0 signifies full
     * transparency and 1.0 signifies full opacity.
     */
    val alpha: @Range(from = 0, to = 1) Double

    /**
     * Interpolate with other color.
     *
     * @param other Color for interpolation.
     * @param progress Progress of interpolation in ``0.0..1.0`` range.
     *
     * @return New instance of [Color] with new values.
     *
     * @throws IllegalArgumentException If [progress] is not in ``0.0..1.0`` range.
     */
    @Throws(IllegalArgumentException::class)
    override fun interpolate(other: Color, progress: @Range(from = 0, to = 1) Float): Color

    /**
     * Multiply values in color.
     *
     * @param value Multiply value.
     *
     * @return New instance of [Color].
     */
    operator fun times(value: Float): Color = times(value.toDouble())

    /**
     * Multiply values in color.
     *
     * @param value Multiply value.
     *
     * @return New instance of [Color].
     */
    operator fun times(value: Double): Color

    /**
     * Converts the current color instance into its integer representation.
     *
     * @return The integer representation of the color.
     */
    fun toInt(): Int

    /**
     * Copy current instance of [Color] with it values.
     *
     * @return New instance of [Color].
     */
    override fun copy(): Color

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        @Throws(IllegalArgumentException::class)
        fun create(
            red:   @Range(from = 0, to = 255) Int,
            green: @Range(from = 0, to = 255) Int,
            blue:  @Range(from = 0, to = 255) Int,
            alpha: @Range(from = 0, to = 1) Double
        ): Color
    }

    @Suppress("UndocumentedPublicProperty")
    companion object {

        @JvmField val BLACK = of(0, 0, 0)
        @JvmField val DARK_BLUE = of(0, 0, 170)
        @JvmField val DARK_GREEN = of(0, 170, 0)
        @JvmField val DARK_AQUA = of(0, 170, 170)
        @JvmField val DARK_RED = of(170, 0, 0)
        @JvmField val DARK_PURPLE = of(170, 0, 170)
        @JvmField val GOLD = of(255, 170, 0)
        @JvmField val GRAY = of(170, 170, 170)
        @JvmField val DARK_GRAY = of(85, 85, 85)
        @JvmField val BLUE = of(85, 85, 255)
        @JvmField val RED = of(255, 85, 85)
        @JvmField val YELLOW = of(255, 255, 85)
        @JvmField val GREEN = of(85, 255, 85)
        @JvmField val AQUA = of(85, 255, 255)
        @JvmField val LIGHT_PURPLE = of(255, 85, 255)
        @JvmField val WHITE = of(255, 255, 255)

        /**
         * Fully transparent color.
         */
        @JvmField val TRANSPARENT = of(0, 0, 0, 0.0)

        /**
         * Create instance of [Color].
         *
         * @param red Red value.
         * @param green Green value.
         * @param blue Blue value.
         * @param alpha Alpha value.
         *
         * @return New instance of [Color].
         *
         * @throws IllegalArgumentException If some value is not in its range.
         */
        @JvmStatic
        fun of(
            red:   @Range(from = 0, to = 255) Int = 255,
            green: @Range(from = 0, to = 255) Int = 255,
            blue:  @Range(from = 0, to = 255) Int = 255,
            alpha: @Range(from = 0, to = 1) Double = 1.0
        ): Color = single<Factory>().create(red, green, blue, alpha)
    }
}
