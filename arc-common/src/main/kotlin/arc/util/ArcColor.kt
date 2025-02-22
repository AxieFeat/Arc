package arc.util

data class ArcColor(
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

    override fun toInt(): Int {
        val alphaInt = (alpha * 255).toInt().coerceIn(0, 255)
        return java.awt.Color(red, green, blue, alphaInt).rgb
    }

    override fun copy(): Color {
        return ArcColor(this.red, this.green, this.blue, this.alpha)
    }

    object Factory : Color.Factory {
        override fun create(red: Int, green: Int, blue: Int, alpha: Double): Color {
            return ArcColor(red, green, blue, alpha)
        }

    }

}