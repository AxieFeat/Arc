package arc.gles.device

import arc.device.GPU
import arc.device.OshiDevice
import org.lwjgl.opengles.GLES20.GL_RENDERER
import org.lwjgl.opengles.GLES20.glGetString

internal object GlesOshiDevice : OshiDevice() {

    override fun findUsedGpu(): GPU? {
        val renderer = glGetString(GL_RENDERER)?.trim() ?: "Unknown renderer"

        return gpu.find {
            it.name.trim().equals(renderer, true)
        }
    }
}
