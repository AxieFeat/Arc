package arc.gl.graphics

import org.lwjgl.opengl.GL41

internal object GlHelper {

    const val DEFAULT_TEX: Int = 33984

    @JvmStatic
    fun setActiveTexture(texture: Int) {
        GL41.glClientActiveTexture(texture)
    }

}