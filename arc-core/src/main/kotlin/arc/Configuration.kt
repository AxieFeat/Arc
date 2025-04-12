package arc

import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents configuration of Arc.
 */
@ImmutableType
interface Configuration {

    /**
     * Name of window.
     */
    val windowName: String

    /**
     * Width of window.
     */
    val windowWidth: Int

    /**
     * Height of window.
     */
    val windowHeight: Int

    /**
     * A bit depth of window.
     */
    val windowBitDepth: Int

    /**
     * Refresh rate limit of window.
     */
    val windowRefreshRate: Int

    /**
     * Is window in fullscreen.
     */
    val windowFullScreen: Boolean

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(
            windowName: String,
            windowWidth: Int,
            windowHeight: Int,
            windowBitDepth: Int,
            windowRefreshRate: Int,
            windowFullScreen: Boolean
        ): Configuration

    }

    companion object {

        /**
         * Create new instance of [Configuration].
         *
         * @param windowName Name of window.
         * @param windowWidth Width of window.
         * @param windowHeight Height of window.
         * @param windowBitDepth Bit depth of window.
         * @param windowRefreshRate Limit of refresh rate for window.
         * @param windowFullScreen Is window in fullscreen.
         *
         * @return New instance of [Configuration].
         */
        @JvmStatic
        fun create(
            windowName: String = "Arc engine",
            windowWidth: Int = 720,
            windowHeight: Int = 420,
            windowBitDepth: Int = 8,
            windowRefreshRate: Int = 60,
            windowFullScreen: Boolean = false
        ): Configuration {
            return Arc.factory<Factory>().create(
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