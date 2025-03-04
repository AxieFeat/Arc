package arc.demo.bind

import arc.input.KeyCode
import arc.input.mouse.MouseBinding

object KeyLogger : MouseBinding {

    override val id: String = "arc.demo.logger"
    override val key: KeyCode = KeyCode.ANY

    override fun onPress(key: KeyCode) {
        println("Key: $key")
    }

    override fun onRelease(key: KeyCode) {

    }

    override fun onScroll(xOffset: Double, yOffset: Double) {

    }
}