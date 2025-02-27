package arc.graphics

abstract class ArcScene : Scene {

    override var inUse: Boolean = false
    override var isSkipRender: Boolean = false
    override var showCursor: Boolean = true

    override fun render() {}

}