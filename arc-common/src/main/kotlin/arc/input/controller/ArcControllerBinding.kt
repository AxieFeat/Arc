package arc.input.controller

import arc.input.AbstractBinding
import arc.input.KeyCode

internal class ArcControllerBinding(
    id: String,
    key: KeyCode,
) : AbstractBinding(id, key), ControllerBinding {

    override fun onAxis(value: Float) {

    }

    override fun onPress() {

    }

    override fun onRelease() {

    }

}