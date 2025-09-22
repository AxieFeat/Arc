package arc.gl.texture

import arc.texture.TextureAtlas

internal class GlTextureAtlas : GlTexture(), TextureAtlas {

    override var width: Int = -1
    override var height: Int = -1

    override fun uv(x: Int, y: Int): Pair<Float, Float> {
        return x / width.toFloat() to y / height.toFloat()
    }
}
