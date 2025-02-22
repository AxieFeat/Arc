package arc.window

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.math.Point2i
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a mutable window interface.
 *
 * This interface defines the structure and functionality for handling windows,
 * including their properties such as position, size, and event handling.
 * It provides the necessary functions for creating, resizing, and closing windows.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Window {

    /**
     * Represents the unique native handle identifier for the window.
     *
     * This property provides access to the internal native handle of the
     * window, which is typically used for platform-specific operations
     * or interactions with third-party libraries.
     */
    @get:JvmName("handle")
    val handle: Long

    /**
     * Represents the handler for window events.
     *
     * This property allows specifying a custom implementation of the `WindowHandler` interface
     * that responds to window-related events such as resizing, focus changes, cursor interactions,
     * and movements. It can be set or retrieved to enable customization of the window behavior.
     *
     * The handler's methods will be invoked accordingly when relevant events occur in the associated `Window` instance.
     */
    @get:JvmName("handler")
    var handler: WindowHandler

    /**
     * The name of the window.
     *
     * Represents the title or identifier associated with the window instance.
     * This property is immutable and reflects the name assigned at the time of creation.
     */
    @get:JvmName("name")
    val name: String

    /**
     * Represents the current width of the window.
     *
     * This property provides the width of the window in pixels. It can be used
     * to query the current horizontal size of the window, for example, to adjust
     * rendering or layout configurations dynamically based on the size of the
     * window.
     */
    @get:JvmName("width")
    val width: Int

    /**
     * Represents the height of the window in pixels.
     *
     * The height property defines the vertical size of the window's drawable area.
     * It can be used to determine the current height of the window or to reference
     * when adjusting other window-related parameters, such as viewport dimensions.
     */
    @get:JvmName("height")
    val height: Int

    /**
     * Represents the position of the window in a 2D coordinate space.
     *
     * The position is defined by a [Point2i] object, which contains the X and Y
     * integer coordinates of the window's top-left corner on the screen.
     * This property is mutable and reflects the current location of the window.
     */
    @get:JvmName("position")
    val position: Point2i

    /**
     * Indicates whether the window currently has input focus.
     *
     * This property is `true` if the window is in focus and able to receive input
     * events. When the focus state of the window changes, the corresponding handler
     * in the `WindowHandler` will be triggered. It is a read-only property.
     */
    val isFocus: Boolean

    /**
     * Indicates whether the window can be resized by the user.
     *
     * This property defines if the current window's dimensions can be adjusted
     * dynamically, typically by the user through interaction with the window
     * borders or controls. Setting this property to `true` allows resizing, while
     * setting it to `false` makes the window fixed in its current dimensions.
     */
    var isResizable: Boolean

    /**
     * Indicates whether the window is currently hidden.
     *
     * This property can be used to check or modify the visibility status of the window.
     * When set to `true`, the window is considered hidden; when `false`, the window is visible.
     */
    var isHide: Boolean

    /**
     * Resizes the window to the specified width and height.
     *
     * @param width The new width of the window.
     * @param height The new height of the window.
     */
    fun resize(width: Int, height: Int)

    /**
     * Creates the window with the specified properties.
     *
     * This method initializes the window, making it ready for rendering
     * and interaction. It configures internal properties and may also
     * involve setting up underlying APIs or libraries.
     *
     * Invoking this method on an already created window may result in
     * undefined behavior or exceptions.
     *
     * Ensure the window's essential properties like width, height, name,
     * and handler are set before calling this method.
     */
    fun create()

    /**
     * Closes the window and releases any associated resources.
     *
     * This method should be called to properly shut down and clean up a window instance.
     * Once this method is invoked, the window can no longer be used for rendering or interactions.
     * Ensure all necessary operations are completed before calling.
     */
    fun close()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

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