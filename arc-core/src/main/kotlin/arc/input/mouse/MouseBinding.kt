package arc.input.mouse

import arc.annotations.ImmutableType
import arc.input.Binding
import arc.input.KeyCode

/**
 * This interface represents binding for mouse.
 *
 * @see Binding
 * @see MouseInput
 */
@ImmutableType
interface MouseBinding : Binding {

    /**
     * Calls when [key] is [KeyCode.KEY_SCROLL] and mouse is scrolling.
     *
     * @param xOffset X offset of scrolling.
     * @param yOffset Y offset of scrolling.
     */
    fun onScroll(xOffset: Double, yOffset: Double)

}