package arc.graphics

object EmptyScene : Scene {

    override val drawer: Drawer
        get() = throw UnsupportedOperationException("Can not find drawer in empty scene!")
    override val camera: Camera
        get() = Camera.of(60f, 0f, 0f)
    override val fps: Int
        get() = 0
    override val delta: Float
        get() = 0f
    override var isShowCursor: Boolean
        get() = true
        set(value) {}

    override fun render() {

    }
}