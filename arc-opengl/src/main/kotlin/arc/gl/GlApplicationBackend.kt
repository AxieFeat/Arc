package arc.gl

import arc.AbstractApplicationBackend
import org.lwjgl.opengl.GL41.*

internal object GlApplicationBackend : AbstractApplicationBackend("opengl") {

    private val iGpuKeywords = listOf("UHD", "Iris", "HD Graphics", "Apple", "Intel")

    private val glRenderer
        get() = glGetString(GL_RENDERER) ?: "unknown"

    override val version: String
        get() = glGetString(GL_VERSION) ?: "unknown"

    override val isIGpu: Boolean
        get() = iGpuKeywords.any { keyword ->
            glRenderer.contains(keyword, ignoreCase = true)
        }

}