package arc.gles

import arc.AbstractApplicationBackend
import org.lwjgl.opengles.GLES20.GL_RENDERER
import org.lwjgl.opengles.GLES20.GL_VERSION
import org.lwjgl.opengles.GLES20.glGetString

internal object GlesApplicationBackend : AbstractApplicationBackend("opengl") {

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