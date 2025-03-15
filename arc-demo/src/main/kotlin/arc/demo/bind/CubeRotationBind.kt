package arc.demo.bind

import arc.input.Binding
import arc.input.KeyCode

class CubeRotationBind : Binding {

    var state: Boolean = false
        private set

    override val id: String = "arc.demo.rotation"
    override val key: KeyCode = KeyCode.KEY_E

    override fun onPress(key: KeyCode) {
        state = !state
    }

}