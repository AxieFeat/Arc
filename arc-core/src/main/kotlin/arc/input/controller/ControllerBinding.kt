package arc.input.controller

import arc.annotations.ImmutableType
import arc.input.Binding
import arc.input.KeyType

/**
 * This interface represents binding for controller.
 *
 * @see Binding
 * @see ControllerInput
 */
@ImmutableType
interface ControllerBinding : Binding {

    /**
     * If [key] type is [KeyType.CONTROLLER].
     *
     * Calls when axis are changed.
     *
     * @param value Value of axis.
     */
    fun onAxis(value: Float)

}