package arc.util

import arc.util.pattern.AbstractInterpolatable
import kotlin.math.roundToInt

internal data class SimpleColor(
    override val red: Int = MAX_RGB_VALUE,
    override val green: Int = MAX_RGB_VALUE,
    override val blue: Int = MAX_RGB_VALUE,
    override val alpha: Double = MAX_ALPHA_VALUE
) : AbstractInterpolatable<Color>(), Color {

    init {
        require(red in MIN_RGB_VALUE..MAX_RGB_VALUE) { "Red value is not in $RGB_PRETTY_RANGE range!" }
        require(green in MIN_RGB_VALUE..MAX_RGB_VALUE) { "Green value is not in $RGB_PRETTY_RANGE range!" }
        require(blue in MIN_RGB_VALUE..MAX_RGB_VALUE) { "Blue value is not in $RGB_PRETTY_RANGE range!" }
        require(alpha in MIN_ALPHA_VALUE..MAX_ALPHA_VALUE) { "Alpha value is not in $ALPHA_PRETTY_RANGE range!" }
    }

    override fun times(value: Double): Color {
        return SimpleColor(
            (this.red * value).toInt().coerceIn(MIN_RGB_VALUE, MAX_RGB_VALUE),
            (this.green * value).toInt().coerceIn(MIN_RGB_VALUE, MAX_RGB_VALUE),
            (this.blue * value).toInt().coerceIn(MIN_RGB_VALUE, MAX_RGB_VALUE),
            this.alpha * value.coerceIn(MIN_ALPHA_VALUE, MAX_ALPHA_VALUE)
        )
    }

    override fun toInt(): Int {
        val alphaInt = (alpha * MAX_RGB_VALUE).toInt().coerceIn(MIN_RGB_VALUE, MAX_RGB_VALUE)
        return java.awt.Color(red, green, blue, alphaInt).rgb
    }

    override fun copy(): Color {
        return SimpleColor(this.red, this.green, this.blue, this.alpha)
    }

    override fun doInterpolation(other: Color, progress: Float): SimpleColor {
        val progress = progress.toDouble()

        return SimpleColor(
            (red + (other.red - red) * progress).roundToInt(),
            (green + (other.green - green) * progress).roundToInt(),
            (blue + (other.blue - blue) * progress).roundToInt(),
            this.alpha * (other.alpha - alpha) * progress.coerceIn(MIN_ALPHA_VALUE, MAX_ALPHA_VALUE)
        )
    }

    object Factory : Color.Factory {

        override fun create(red: Int, green: Int, blue: Int, alpha: Double): Color {
            return SimpleColor(red, green, blue, alpha)
        }
    }

    companion object {

        const val MAX_RGB_VALUE = 255
        const val MIN_RGB_VALUE = 0

        const val MIN_ALPHA_VALUE = 0.0
        const val MAX_ALPHA_VALUE = 1.0

        private const val RGB_PRETTY_RANGE = "$MIN_RGB_VALUE..$MAX_RGB_VALUE"
        private const val ALPHA_PRETTY_RANGE = "$MIN_ALPHA_VALUE..$MAX_ALPHA_VALUE"
    }
}
