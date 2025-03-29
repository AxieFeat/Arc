package arc.window

/**
 * This interface represents event handler of window.
 */
interface WindowHandler {

    /**
     * Handles the focus state of a window.
     */
    fun focus(focus: Boolean) {}

    /**
     * Handles the resize of window.
     */
    fun resize(width: Int, height: Int) {}

    /**
     * Handles the event triggered when the cursor enters the window area.
     */
    fun cursorEntered() {}

    /**
     * Handles the event triggered when the cursor leaves the window area.
     */
    fun cursorLeaved() {}

    /**
     * Handles cursor moving in window.
     */
    fun cursorMove(x: Double, y: Double) {}

    /**
     * Handles cursor scrolling in window.
     */
    fun scroll(x: Double, y: Double) {}

    /**
     * Handles moving of window.
     */
    fun windowMove(x: Int, y: Int) {}

}