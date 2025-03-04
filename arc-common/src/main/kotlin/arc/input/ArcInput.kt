package arc.input

import arc.Application
import arc.input.keyboard.ArcKeyboardInput
import arc.input.mouse.ArcMouseInput

object ArcInput {

    @JvmStatic
    fun install(application: Application) {
        ArcMouseInput.window = application.window
        ArcKeyboardInput.window = application.window
    }

}