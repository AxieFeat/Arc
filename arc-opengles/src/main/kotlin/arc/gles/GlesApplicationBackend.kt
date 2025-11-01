package arc.gles

import arc.AbstractApplicationBackend
import arc.gles.device.GlesOshiDevice
import org.lwjgl.opengles.GLES20.GL_VERSION
import org.lwjgl.opengles.GLES20.glGetString

internal object GlesApplicationBackend : AbstractApplicationBackend("opengl", GlesOshiDevice) {

    override val version: String
        get() = glGetString(GL_VERSION) ?: "unknown"
}
