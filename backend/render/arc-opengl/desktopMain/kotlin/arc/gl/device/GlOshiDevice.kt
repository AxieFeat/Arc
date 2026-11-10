package arc.gl.device

import arc.device.GPU
import arc.device.OshiDevice
import org.lwjgl.opengl.GL11

internal object GlOshiDevice : OshiDevice() {

    override fun findUsedGpu(): GPU? {
        val renderer = GL11.glGetString(GL11.GL_RENDERER)?.trim() ?: "Unknown renderer"

        return gpu.find {
            it.name.trim().equals(renderer, true)
        }
    }
}
