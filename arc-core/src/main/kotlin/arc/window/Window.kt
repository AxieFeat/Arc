package arc.window

import arc.Arc
import arc.annotations.TypeFactory
import arc.math.Point2i
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents window of application.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface Window {

    /**
     * Represents the unique native handle identifier for the window.
     */
    val handle: Long

    /**
     * Represents the handler for window events.
     */
    var handler: WindowHandler

    /**
     * The name of the window.
     */
    var name: String

    /**
     * Represents the current width of the window.
     */
    val width: Int

    /**
     * Represents the height of the window in pixels.
     */
    val height: Int

    /**
     * Represents the position of the window in a 2D coordinate space.
     */
    val position: Point2i

    /**
     * Indicates whether the window currently has input focus.
     */
    @get:JvmName("isFocus")
    val isFocus: Boolean

    /**
     * Indicates whether the window can be resized by the user.
     */
    @get:JvmName("isResizable")
    @set:JvmName("setResizable")
    var isResizable: Boolean

    /**
     * Indicates whether the window is currently hidden.
     */
    @get:JvmName("isHide")
    @set:JvmName("setHide")
    var isHide: Boolean

    /**
     * Indicates whether the window has Vsync.
     */
    @get:JvmName("isVsync")
    @set:JvmName("setVsync")
    var isVsync: Boolean

    /**
     * Resizes the window to the specified width and height.
     *
     * @param width The new width of the window.
     * @param height The new height of the window.
     */
    fun resize(width: Int, height: Int)

    /**
     * Initialize window.
     */
    fun create()

    /**
     * Closes the window and releases any associated resources.
     */
    fun close()

    /**
     * Is window should close.
     */
    fun shouldClose(): Boolean

    /**
     * Make some updates in frame begin.
     */
    fun beginFrame()

    /**
     * Make some updates in frame end.
     */
    fun endFrame()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(name: String, handler: WindowHandler, width: Int, height: Int): Window

    }

    companion object {

        /**
         * Create new instance of [Window].
         *
         * @param name Name of window.
         * @param handler Event handler of window.
         * @param width Width of window.
         * @param height Height of window.
         *
         * @return New instance of [Window].
         */
        @JvmStatic
        fun create(name: String, handler: WindowHandler, width: Int, height: Int): Window {
            return Arc.factory<Factory>().create(name, handler, width, height)
        }

    }

}