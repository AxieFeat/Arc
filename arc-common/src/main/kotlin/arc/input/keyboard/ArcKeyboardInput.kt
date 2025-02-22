package arc.input.keyboard

import arc.input.ArcBindingProcessor
import arc.input.BindingProcessor
import arc.input.KeyCode

class ArcKeyboardInput(
    override val bindingProcessor: BindingProcessor = ArcBindingProcessor(),
) : KeyboardInput {

    override fun isPressed(key: KeyCode): Boolean {
        return true
    }

    override fun isReleased(key: KeyCode): Boolean {
        return true
    }

}