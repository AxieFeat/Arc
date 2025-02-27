package arc.graphics

object EmptyScene : Scene {
    override val drawer: Drawer
        get() = TODO("Not yet implemented")
    override val fps: Int
        get() = 0
    override val delta: Float
        get() = 0f
    override val inUse: Boolean
        get() = false
    override var isSkipRender: Boolean
        get() = true
        set(value) {}
    override var showCursor: Boolean
        get() = true
        set(value) {}

    override fun render() {

    }
}