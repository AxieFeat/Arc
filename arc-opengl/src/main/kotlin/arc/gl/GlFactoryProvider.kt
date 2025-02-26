package arc.gl

import arc.ArcFactoryProvider
import arc.assets.TextureAsset
import arc.gl.asset.GlTextureAsset
import arc.gl.graphics.GlDrawBuffer
import arc.gl.graphics.GlScene
import arc.gl.graphics.GlShaderInstance
import arc.gl.graphics.GlTexture
import arc.graphics.DrawBuffer
import arc.graphics.Scene
import arc.shader.ShaderInstance
import arc.texture.Texture
import arc.register

object GlFactoryProvider {

    private val provider = ArcFactoryProvider

    @JvmStatic
    fun bootstrap() {
        provider.register<TextureAsset.Factory>(GlTextureAsset.Factory)
        provider.register<Texture.Factory>(GlTexture.Factory)

        provider.register<ShaderInstance.Factory>(GlShaderInstance.Factory)
        provider.register<DrawBuffer.Factory>(GlDrawBuffer.Factory)

        provider.register<Scene.Factory>(GlScene.Factory)
    }
}