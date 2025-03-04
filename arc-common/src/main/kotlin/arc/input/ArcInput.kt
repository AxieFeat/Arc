package arc.input

import arc.Application
import arc.input.keyboard.ArcKeyboardInput
import arc.input.keyboard.ArcKeyboardInput.isPressed
import arc.input.mouse.ArcMouseInput

object ArcInput {

    @JvmStatic
    fun install(application: Application) {
        ArcMouseInput.window = application.window
        ArcKeyboardInput.window = application.window

        updateControllers()
    }

    @JvmStatic
    fun updateControllers() {

    }

    @JvmStatic
    internal fun checkForAll(keys: Set<KeyCode>, pressed: Boolean): Boolean {
        if(!pressed) return false

        var status = true

        keys.forEach { key ->
            if(!isPressed(key)) {
                status = false
            }
        }

        return status
    }

}