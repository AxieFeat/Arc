package arc.input.mouse

import arc.input.BindingProcessor
import arc.input.KeyCode
import arc.math.Point2d

internal class ArcMouseInput(
    override val bindingProcessor: BindingProcessor
) : MouseInput {

    override var position: Point2d = Point2d.ZERO

    override fun isPressed(key: KeyCode): Boolean {
        return true
    }

    override fun isReleased(key: KeyCode): Boolean {
        return false
    }


}