package arc.gles.graphics

import arc.gles.GlesApplication
import arc.graphics.*
import arc.graphics.scene.Scene
import arc.shader.ShaderInstance
import arc.texture.EmptyTexture
import arc.texture.Texture
import org.lwjgl.opengles.GLES20.GL_BLEND
import org.lwjgl.opengles.GLES20.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengles.GLES20.GL_CULL_FACE
import org.lwjgl.opengles.GLES20.GL_DEPTH_BUFFER_BIT
import org.lwjgl.opengles.GLES20.GL_DEPTH_TEST
import org.lwjgl.opengles.GLES20.GL_TEXTURE_2D
import org.lwjgl.opengles.GLES20.glBlendEquation
import org.lwjgl.opengles.GLES20.glBlendFunc
import org.lwjgl.opengles.GLES20.glBlendFuncSeparate
import org.lwjgl.opengles.GLES20.glClear
import org.lwjgl.opengles.GLES20.glClearColor
import org.lwjgl.opengles.GLES20.glColorMask
import org.lwjgl.opengles.GLES20.glDepthMask
import org.lwjgl.opengles.GLES20.glDisable
import org.lwjgl.opengles.GLES20.glEnable
import org.lwjgl.opengles.GLES20.glViewport

internal object GlesRenderSystem : RenderSystem {

    override var shader: ShaderInstance = EmptyShaderInstance
    override var texture: Texture = EmptyTexture

    override val drawer: Drawer = GlesDrawer

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
        GlesApplication.window.pollEvents()

        clear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        resetViewport()
    }

    override fun endFrame() {
        shader.unbind()
        shader = EmptyShaderInstance
        texture.unbind()
        texture = EmptyTexture
        GlesApplication.window.swapBuffers()
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
        setViewport(0, 0, GlesApplication.window.width, GlesApplication.window.height)
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