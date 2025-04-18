package arc.gl

import arc.AbstractPlatform
import org.lwjgl.opengl.GL11.GL_RENDERER
import org.lwjgl.opengl.GL11.glGetString

internal object GlPlatform : AbstractPlatform("opengl") {

    private val iGpuKeywords = listOf("UHD", "Iris", "HD Graphics", "Apple", "Intel")

    private val glRenderer
        get() = glGetString(GL_RENDERER) ?: ""

    override val isIGpu: Boolean
        get() = iGpuKeywords.any { keyword ->
            glRenderer.contains(keyword, ignoreCase = true)
        }

}