package arc.gl.texture

import org.lwjgl.opengl.GL41

internal object TextureUtil {

    @JvmStatic
    fun glGenTextures(): Int {
        return GL41.glGenTextures()
    }

    @JvmStatic
    fun deleteTexture(textureId: Int) {
        GL41.glDeleteTextures(textureId)
    }

}