package arc.gles.graphics

import arc.graphics.TextureSampler
import arc.graphics.TextureSampling
import org.lwjgl.opengles.GLES20.GL_LINEAR
import org.lwjgl.opengles.GLES20.GL_NEAREST
import org.lwjgl.opengles.GLES20.GL_TEXTURE_MAG_FILTER
import org.lwjgl.opengles.GLES20.GL_TEXTURE_MIN_FILTER
import org.lwjgl.opengles.GLES20.GL_TEXTURE_WRAP_S
import org.lwjgl.opengles.GLES20.GL_TEXTURE_WRAP_T
import org.lwjgl.opengles.GLES30.GL_CLAMP_TO_EDGE
import org.lwjgl.opengles.GLES30.glDeleteSamplers
import org.lwjgl.opengles.GLES30.glGenSamplers
import org.lwjgl.opengles.GLES30.glSamplerParameteri

internal class GlesTextureSampler(sampling: TextureSampling) : GlesContextResource(), TextureSampler {

    private var id = glGenSamplers()

    init {
        val filter = if (sampling == TextureSampling.LINEAR) GL_LINEAR else GL_NEAREST
        glSamplerParameteri(id, GL_TEXTURE_MIN_FILTER, filter)
        glSamplerParameteri(id, GL_TEXTURE_MAG_FILTER, filter)
        glSamplerParameteri(id, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glSamplerParameteri(id, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
    }

    fun checkedId(): Int {
        requireCurrentContext()
        check(id != 0) { "Texture sampler is closed" }
        return id
    }

    override fun cleanup() {
        if (id != 0) {
            if (isCurrentContext()) glDeleteSamplers(id)
            id = 0
        }
    }
}
