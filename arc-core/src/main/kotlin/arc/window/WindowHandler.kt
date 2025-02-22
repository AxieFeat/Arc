package arc.window

/**
 * Interface representing event handling for a window.
 *
 * This interface defines the behavior for handling various events that can occur
 * within a window, such as focus changes, cursor interactions, resizing, or moving
 * the window. Implementations of this interface should provide functionality
 * corresponding to the defined methods to support event-driven management of
 * related window operations.
 */
interface WindowHandler {

    /**
     * Handles the focus state of a window.
     *
     * This method is invoked when the focus state of a window changes. Implementations
     * can provide custom behavior for cases where the window gains or loses focus.
     *
     * @param focus Indicates whether the window is now in focus. `true` if the window has gained focus,
     *              `false` if it has lost focus.
     */
    fun focus(focus: Boolean) {}

    /**
     * Resizes the dimensions of a window.
     *
     * This method adjusts the width and height of the window to the specified values.
     * Resizing a window might trigger rendering updates or event notifications depending on the implementation.
     *
     * @param width The desired new width of the window in pixels. Must be a positive integer.
     * @param height The desired new height of the window in pixels. Must be a positive integer.
     */
    fun resize(width: Int, height: Int) {}

    /**
     * Handles the event triggered when the cursor enters the window area.
     *
     * This method is invoked when the cursor transitions from being outside the window
     * to within the boundaries of the window. Implementations can use this to perform
     * specific actions such as changing the appearance of the cursor, starting certain
     * animations, or handling specific UI interactions.
     */
    fun cursorEntered() {}

    /**
     * Handles the event triggered when the cursor leaves the window area.
     *
     * This method is invoked when the cursor transitions from being within the boundaries
     * of the window to outside of it. Implementations can use this to perform specific actions
     * such as restoring the default cursor appearance, stopping animations, or managing other
     * UI interactions.
     */
    fun cursorLeaved() {}

    /**
     * Moves the cursor to the specified position within the window.
     *
     * This method adjusts the cursor's position to the given coordinates,
     * which are relative to the window's coordinate system. It can be used
     * to update cursor movements or simulate cursor transitions.
     *
     * @param x The X-coordinate to move the cursor to, in the window's coordinate space.
     * @param y The Y-coordinate to move the cursor to, in the window's coordinate space.
     */
    fun cursorMove(x: Double, y: Double) {}

    /**
     * Scrolls the content within the window by the specified offsets.
     *
     * This method modifies the scroll position of the content displayed within the window.
     * The parameters represent the horizontal and vertical scroll offsets. Positive values
     * indicate scrolling right and down, while negative values indicate scrolling left and up.
     *
     * @param x The horizontal scroll offset. Positive values scroll right, negative values scroll left.
     * @param y The vertical scroll offset. Positive values scroll down, negative values scroll up.
     */
    fun scroll(x: Double, y: Double) {}

    /**
     * Moves the window to a specified position on the screen.
     *
     * This method sets the window's top-left corner to the specified coordinates
     * in the screen's coordinate space. The coordinates are in pixels and represent
     * absolute positions.
     *
     * @param x The X-coordinate for the new position of the window.
     * @param y The Y-coordinate for the new position of the window.
     */
    fun windowMove(x: Int, y: Int) {}

}