package arc.gl.display

import arc.display.Display
import arc.input.mouse.VirtualMouseInput
import arc.math.AABB
import arc.shader.FrameBuffer
import arc.shader.ShaderInstance

data class GlDisplay(
    override val aabb: AABB
) : Display {

    override val width: Int = (aabb.max.x - aabb.min.x).toInt().coerceAtLeast(1)
    override val height: Int = (aabb.max.y - aabb.min.y).toInt().coerceAtLeast(1)

    private val frameBuffer = FrameBuffer.create(
        width = width,
        height = height,
        useDepth = true
    )

    override val mouse: VirtualMouseInput = VirtualMouseInput.create(this)

    override fun render(shader: ShaderInstance) {
        shader.bind()
        frameBuffer.render()
        shader.unbind()
        frameBuffer.clear()
    }

    override fun bind() {
        frameBuffer.bind(true)
    }

    override fun unbind() {
        frameBuffer.unbind()
    }

    override fun cleanup() {
        frameBuffer.cleanup()
    }

    object Factory : Display.Factory {
        override fun create(aabb: AABB): Display {
            return GlDisplay(aabb)
        }

    }

}