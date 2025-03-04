package arc.input

import arc.annotations.ImmutableType
import arc.input.mouse.MouseBinding

/**
 * This interface represents some binding for key.
 *
 * For creation instance you need create own implementation of this interface.
 *
 * @see MouseBinding
 * @see ControllerBinding
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Binding : BindingLike {

    /**
     * ID of this binding.
     */
    @get:JvmName("id")
    override val id: String

    /**
     * Key of binding.
     */
    @get:JvmName("key")
    val key: KeyCode

    /**
     * Calls when this key is pressed
     */
    fun onPress(key: KeyCode)

    /**
     * Calls when this key is released.
     */
    fun onRelease(key: KeyCode)

}