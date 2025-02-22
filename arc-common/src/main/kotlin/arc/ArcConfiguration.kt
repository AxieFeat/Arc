package arc

data class ArcConfiguration(
    override val windowName: String = "Arc engine",
    override val windowWidth: Int = 720,
    override val windowHeight: Int = 420,
    override val windowBitDepth: Int = 8,
    override val windowRefreshRate: Int = 60,
    override val windowFullScreen: Boolean = false
) : Configuration {

    object Factory : Configuration.Factory {
        override fun create(
            windowName: String,
            windowWidth: Int,
            windowHeight: Int,
            windowBitDepth: Int,
            windowRefreshRate: Int,
            windowFullScreen: Boolean
        ): Configuration {
            return ArcConfiguration(
                windowName,
                windowWidth,
                windowHeight,
                windowBitDepth,
                windowRefreshRate,
                windowFullScreen
            )
        }

    }

}