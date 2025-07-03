package arc.util

import kotlin.math.roundToInt

internal data class SimpleColor(
    override val red: Int = 255,
    override val green: Int = 255,
    override val blue: Int = 255,
    override val alpha: Double = 1.0
) : Color {

    init {
        require(red in 0..255) { "Red value is not in 0..255 range!" }
        require(green in 0..255) { "Green value is not in 0..255 range!" }
        require(blue in 0..255) { "Blue value is not in 0..255 range!" }
        require(alpha in 0.0..1.0) { "Alpha value is not in 0.0..1.0 range!" }
    }

    override fun times(value: Float): Color {
        return SimpleColor(
            (this.red * value).toInt().coerceIn(0, 255),
            (this.green * value).toInt().coerceIn(0, 255),
            (this.blue * value).toInt().coerceIn(0, 255),
            this.alpha * value.coerceIn(0f, 1f)
        )
    }

    override fun toInt(): Int {
        val alphaInt = (alpha * 255).toInt().coerceIn(0, 255)
        return java.awt.Color(red, green, blue, alphaInt).rgb
    }

    override fun copy(): Color {
        return SimpleColor(this.red, this.green, this.blue, this.alpha)
    }

    override fun interpolate(other: Color, progress: Float): Color {
        require(progress in 0.0..1.0) { "Progress value is not in 0.0..1.0 range!" }

        return SimpleColor(
            (red + (other.red - red) * progress).roundToInt(),
            (green + (other.green - green) * progress).roundToInt(),
            (blue + (other.blue - blue) * progress).roundToInt(),
            this.alpha * (other.alpha - alpha) * progress.coerceIn(0f, 1f)
        )
    }

    object Factory : Color.Factory {
        override fun create(red: Int, green: Int, blue: Int, alpha: Double): Color {
            return SimpleColor(red, green, blue, alpha)
        }
    }

}