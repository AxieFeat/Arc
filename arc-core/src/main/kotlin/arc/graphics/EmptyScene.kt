package arc.graphics

import arc.graphics.scene.Scene

/**
 * Empty realization of [arc.graphics.scene.Scene].
 *
 * Can be used as a stub instead of the nullable type.
 */
object EmptyScene : Scene {

    override val camera: Camera = Camera.of(60f, 0f, 0f)
    override val fps: Int = 0
    override val delta: Float = 0f
    override var isShowCursor: Boolean = true

    override fun render() {}

}