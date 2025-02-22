package arc.input.mouse

import arc.input.AbstractBinding
import arc.input.KeyCode

class ArcMouseBinding(
    id: String,
    key: KeyCode,
) : AbstractBinding(id, key), MouseBinding {

    override fun onScroll(xOffset: Double, yOffset: Double) {

    }

    override fun onPress() {

    }

    override fun onRelease() {

    }

}