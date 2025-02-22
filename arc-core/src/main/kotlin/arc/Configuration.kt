package arc

import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents configuration of Arc.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Configuration {

    /**
     * Name of window.
     */
    @get:JvmName("windowName")
    val windowName: String

    /**
     * Width of window.
     */
    @get:JvmName("windowWidth")
    val windowWidth: Int

    /**
     * Height of window.
     */
    @get:JvmName("windowHeight")
    val windowHeight: Int

    /**
     * A bit depth of window.
     */
    @get:JvmName("windowBitDepth")
    val windowBitDepth: Int

    /**
     * Refresh rate limit of window.
     */
    @get:JvmName("windowRefreshRate")
    val windowRefreshRate: Int

    /**
     * Is window in fullscreen.
     */
    @get:JvmName("windowFullScreen")
    val windowFullScreen: Boolean

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

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