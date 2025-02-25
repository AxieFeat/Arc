package arc.graphics

import arc.graphics.g3d.ChunkContainer

object EmptyScene : Scene {
    override val camera: Camera
        get() = ArcCamera()
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
    override val chunkContainer: ChunkContainer
        get() = ArcChunkContainer()

    override fun render() {

    }
}