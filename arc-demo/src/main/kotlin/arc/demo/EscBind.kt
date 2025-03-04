package arc.demo

import arc.input.Binding
import arc.input.KeyCode

object EscBind : Binding {

    override val id: String = "arc.demo.esc"

    override val key: KeyCode = KeyCode.KEY_ESCAPE

    // onPress have KeyCode parameter because we can set key of binding to KeyCode.ANY
    override fun onPress(key: KeyCode) {
        println("Pressed ${key.name}")
    }

    override fun onRelease(key: KeyCode) {

    }
}