package arc.input

import arc.input.mouse.MouseBinding

/**
 * This interface represents some binding for key.
 *
 * For creation instance you need create own implementation of this interface.
 *
 * @see MouseBinding
 */
interface Binding : BindingLike {

    /**
     * ID of this binding.
     */
    override val id: String

    /**
     * Key of binding.
     */
    val key: KeyCode

    /**
     * Calls when this key is pressed
     */
    fun onPress(key: KeyCode) {}

    /**
     * Calls when this key is released.
     */
    fun onRelease(key: KeyCode) {}

}