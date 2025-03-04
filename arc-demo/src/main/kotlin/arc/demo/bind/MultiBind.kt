package arc.demo.bind

import arc.input.KeyCode
import arc.input.MultiBinding

object MultiBind : MultiBinding {

    override val id: String = "arc.demo.multi"
    override val keys: Set<KeyCode> = setOf(KeyCode.KEY_A, KeyCode.KEY_B)

    override fun onPress() {
        println("You press A + B")
    }

}