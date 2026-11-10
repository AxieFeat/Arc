package arc.extra.graphics.extra

import arc.graphics.Camera

/**
 * Empty realization of [arc.extra.graphics.extra.Scene].
 *
 * Can be used as a stub instead of the nullable type.
 */
@Suppress("EmptyFunctionBlock") // Because it's an empty implementation.
object EmptyScene : Scene {

    override val camera: Camera = Camera.of(60f, 0f, 0f)
    override val fps: Int = 0
    override val delta: Float = 0f
    override var isShowCursor: Boolean = true

    override fun render() {}
}
