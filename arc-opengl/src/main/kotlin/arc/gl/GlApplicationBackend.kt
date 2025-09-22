package arc.gl

import arc.AbstractApplicationBackend
import arc.gl.device.GlOshiDevice
import org.lwjgl.opengl.GL11.GL_VERSION
import org.lwjgl.opengl.GL11.glGetString

internal object GlApplicationBackend : AbstractApplicationBackend("opengl", GlOshiDevice) {

    override val version: String
        get() = glGetString(GL_VERSION) ?: "unknown"
}
