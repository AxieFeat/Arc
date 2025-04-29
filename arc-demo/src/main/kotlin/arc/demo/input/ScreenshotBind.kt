package arc.demo.input

import arc.demo.VoxelGame
import arc.files.classpath
import arc.input.Binding
import arc.input.KeyCode

object ScreenshotBind : Binding {
    override val id: String = "arc.demo.screenshot"
    override val key: KeyCode = KeyCode.KEY_F

    override fun onPress(key: KeyCode) {
        println("Take screenshot")
        VoxelGame.application.screenshot(classpath("screenshots"), "test.png")
    }
}