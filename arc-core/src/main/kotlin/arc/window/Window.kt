package arc.window

import arc.Arc
import arc.annotations.TypeFactory
import arc.math.Point2i
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents a window of the application.
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
     * Info about a backend library of window implementation.
     */
    val backend: WindowBackend

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
     * Get elapsed time since a window is initialized.
     */
    val timeFromInitialize: Double

    /**
     * Indicates whether the window currently has input focus.
     */
    @get:JvmName("isFocus")
    val isFocus: Boolean

    /**
     * Is window can be resized.
     */
    @get:JvmName("isResizable")
    val isResizable: Boolean

    /**
     * Indicates whether the window is currently hidden.
     */
    @get:JvmName("isHidden")
    @set:JvmName("setHidden")
    var isHidden: Boolean

    /**
     * Indicates whether the window has Vsync.
     */
    @get:JvmName("isVsync")
    @set:JvmName("setVsync")
    var isVsync: Boolean

    /**
     * Is the cursor viewable in this window?
     */
    @get:JvmName("isShowCursor")
    @set:JvmName("setShowCursor")
    var isShowCursor: Boolean

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

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(
            name: String,
            handler: WindowHandler,
            width: Int,
            height: Int,
            isResizable: Boolean,
        ): Window

    }

    companion object {

        /**
         * Create new instance of [Window].
         *
         * @param name Name of a window.
         * @param handler Event handler of a window.
         * @param width Width of a window.
         * @param height Height of a window.
         * @param isResizable Is window can be resized.
         *
         * @return New instance of [Window].
         */
        @JvmStatic
        fun of(
            name: String,
            handler: WindowHandler,
            width: Int,
            height: Int,
            isResizable: Boolean = true,
        ): Window {
            return Arc.factory<Factory>().create(name, handler, width, height, isResizable)
        }

    }

}