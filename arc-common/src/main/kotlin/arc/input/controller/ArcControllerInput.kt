package arc.input.controller

import arc.input.ArcBindingProcessor
import arc.input.BindingProcessor
import arc.input.KeyCode

// TODO
internal data class ArcControllerInput(
    override val id: Int,
    override val bindingProcessor: BindingProcessor = ArcBindingProcessor()
) : ControllerInput {
    override fun getAxis(key: KeyCode): Float {
        return 0f
    }

    override fun isPressed(key: KeyCode): Boolean {
        return false
    }

    override fun isReleased(key: KeyCode): Boolean {
       return false
    }
}