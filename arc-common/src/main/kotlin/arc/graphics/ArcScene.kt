package arc.graphics

import arc.graphics.g3d.ChunkContainer

abstract class ArcScene : Scene {
    override val camera: Camera = ArcCamera()

    override var inUse: Boolean = false
    override var isSkipRender: Boolean = false
    override var showCursor: Boolean = true
    override val chunkContainer: ChunkContainer = ArcChunkContainer()

    override fun render() {}
}