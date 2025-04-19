package arc.util

import kotlin.math.roundToInt

internal data class ArcColor(
    override var red: Int = 255,
    override var green: Int = 255,
    override var blue: Int = 255,
    override var alpha: Double = 1.0
) : Color {

    init {
        require(red in 0..255) { "Red value is not in 0..255 range!" }
        require(green in 0..255) { "Green value is not in 0..255 range!" }
        require(blue in 0..255) { "Blue value is not in 0..255 range!" }
        require(alpha in 0.0..1.0) { "Alpha value is not in 0.0..1.0 range!" }
    }

    override fun write(another: Color) {
        this.red = another.red
        this.green = another.green
        this.blue = another.blue
        this.alpha = another.alpha
    }

    override fun mul(value: Float): Color {
        this.red = (this.red * value).toInt().coerceIn(0, 255)
        this.green = (this.green * value).toInt().coerceIn(0, 255)
        this.blue = (this.blue * value).toInt().coerceIn(0, 255)
        this.alpha *= value.coerceIn(0f, 1f)

        return this
    }

    override fun toInt(): Int {
        val alphaInt = (alpha * 255).toInt().coerceIn(0, 255)
        return java.awt.Color(red, green, blue, alphaInt).rgb
    }

    override fun copy(): Color {
        return ArcColor(this.red, this.green, this.blue, this.alpha)
    }

    override fun interpolate(other: Color, progress: Float): Color {
        require(progress in 0.0..1.0) { "Progress value is not in 0.0..1.0 range!" }

        this.red = (red + (other.red - red) * progress).roundToInt()
        this.green = (green + (other.green - green) * progress).roundToInt()
        this.blue = (blue + (other.blue - blue) * progress).roundToInt()
        this.alpha += (other.alpha - alpha) * progress

        return this
    }

    object Factory : Color.Factory {
        override fun create(red: Int, green: Int, blue: Int, alpha: Double): Color {
            return ArcColor(red, green, blue, alpha)
        }

    }

}