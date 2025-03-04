package arc.demo.bind

import arc.input.KeyCode
import arc.input.mouse.MouseBinding

object ScrollBind : MouseBinding {

    override val id: String = "arc.demo.scroll"
    override val key: KeyCode = KeyCode.MOUSE_SCROLL

    override fun onScroll(xOffset: Double, yOffset: Double) {
        println("$id: $xOffset $yOffset")
    }

    override fun onPress(key: KeyCode) {

    }

    override fun onRelease(key: KeyCode) {

    }
}