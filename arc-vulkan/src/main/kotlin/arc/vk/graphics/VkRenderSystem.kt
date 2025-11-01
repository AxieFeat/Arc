package arc.vk.graphics

import arc.graphics.Drawer
import arc.graphics.EmptyScene
import arc.graphics.EmptyShaderInstance
import arc.graphics.RenderSystem
import arc.graphics.scene.Scene
import arc.shader.ShaderInstance
import arc.texture.EmptyTexture
import arc.texture.Texture
import arc.vk.VkApplication

object VkRenderSystem : RenderSystem {

    override var shader: ShaderInstance = EmptyShaderInstance
    override var texture: Texture = EmptyTexture

    override val drawer: Drawer = VkDrawer

    @set:JvmName("_setScene")
    override var scene: Scene = EmptyScene

    override fun bindShader(shader: ShaderInstance) {
        this.shader = shader
        shader.bind()
    }

    override fun bindTexture(texture: Texture) {
        texture.bind()
        this.texture = texture
    }

    override fun beginFrame() {
        VkApplication.window.pollEvents()
        resetViewport()
    }

    override fun endFrame() {
        shader.unbind()
        shader = EmptyShaderInstance
        texture.unbind()
        texture = EmptyTexture
    }

    override fun setScene(scene: Scene) {
        this.scene = scene
    }

    override fun enableDepthTest() {
    }

    override fun disableDepthTest() {
    }

    override fun enableCull() {
    }

    override fun disableCull() {
    }

    override fun enableBlend() {
    }

    override fun disableBlend() {
    }

    override fun colorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean) {
    }

    override fun depthMask(value: Boolean) {
    }

    override fun setViewport(x: Int, y: Int, width: Int, height: Int) {
    }

    override fun resetViewport() {
    }

    override fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
    }

    override fun clear(mask: Int) {
    }

    override fun enableTexture2D() {
    }

    override fun disableTexture2D() {
    }

    override fun blendEquation(mode: Int) {
    }

    override fun blendFuncSeparate(
        sourceFactor: Int,
        destFactor: Int,
        sourceFactorAlpha: Int,
        destFactorAlpha: Int
    ) {
    }

    override fun blendFunc(sourceFactor: Int, destFactor: Int) {
    }
}
