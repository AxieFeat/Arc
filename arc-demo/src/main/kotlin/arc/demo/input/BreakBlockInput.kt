package arc.demo.input

import arc.demo.screen.TerrainScreen
import arc.input.KeyCode
import arc.input.mouse.MouseBinding

object BreakBlockInput : MouseBinding {

    override val id: String = "arc.demo.break-block"

    override val key: KeyCode = KeyCode.MOUSE_LEFT

    override fun onPress(key: KeyCode) {
        TerrainScreen.breakBlock()
    }
}