package arc.gl.graphics

import arc.gl.GlApplication
import arc.graphics.*
import arc.shader.ShaderInstance
import arc.texture.EmptyTexture
import arc.texture.Texture
import org.lwjgl.opengl.GL41.*

internal object GlRenderSystem : RenderSystem {

    override var shader: ShaderInstance = EmptyShaderInstance
    override var texture: Texture = EmptyTexture

    override val drawer: Drawer = GlDrawer

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
        GlApplication.window.beginFrame()

        clear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        resetViewport()
    }

    override fun endFrame() {
        shader.unbind()
        shader = EmptyShaderInstance
        texture.unbind()
        texture = EmptyTexture
        GlApplication.window.endFrame()
    }

    override fun setScene(scene: Scene) {
        this.scene = scene
    }

    override fun enableDepthTest() {
        glEnable(GL_DEPTH_TEST)
    }

    override fun disableDepthTest() {
        glDisable(GL_DEPTH_TEST)
    }

    override fun enableCull() {
        glEnable(GL_CULL_FACE)
    }

    override fun disableCull() {
        glDisable(GL_CULL_FACE)
    }

    override fun enableBlend() {
        glEnable(GL_BLEND)
    }

    override fun disableBlend() {
        glDisable(GL_BLEND)
    }

    override fun polygonMode(face: Int, mode: Int) {
        glPolygonMode(face, mode)
    }

    override fun setShaderColor(r: Float, g: Float, b: Float, a: Float) {
        glColor4f(r, g, b, a)
    }

    override fun clearDepth(depth: Double) {
        glClearDepth(depth)
    }

    override fun colorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean) {
        glColorMask(red, green, blue, alpha)
    }

    override fun depthMask(value: Boolean) {
        glDepthMask(value)
    }

    override fun setViewport(x: Int, y: Int, width: Int, height: Int) {
        glViewport(x, y, width, height)
    }

    override fun resetViewport() {
        setViewport(0, 0, GlApplication.window.width, GlApplication.window.height)
    }

    override fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        glClearColor(red, green, blue, alpha)
    }

    override fun clear(mask: Int) {
        glClear(mask)
    }

    override fun enableTexture2D() {
        glEnable(GL_TEXTURE_2D)
    }

    override fun disableTexture2D() {
        glDisable(GL_TEXTURE_2D)
    }

    override fun blendEquation(mode: Int) {
        glBlendEquation(mode)
    }

    override fun blendFuncSeparate(sourceFactor: Int, destFactor: Int, sourceFactorAlpha: Int, destFactorAlpha: Int) {
        glBlendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha)
    }

    override fun blendFunc(sourceFactor: Int, destFactor: Int) {
        glBlendFunc(sourceFactor, destFactor)
    }
}