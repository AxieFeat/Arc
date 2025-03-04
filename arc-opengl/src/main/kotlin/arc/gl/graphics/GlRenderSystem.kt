package arc.gl.graphics

import arc.gl.GlApplication
import arc.graphics.*
import arc.graphics.vertex.VertexFormatElement
import arc.input.ArcInput
import arc.shader.ShaderInstance
import org.lwjgl.opengl.GL41.*

internal object GlRenderSystem : RenderSystem {

    override var shader: ShaderInstance = EmptyShaderInstance

    override var isDepthTestEnabled: Boolean = true
    override var isBlendEnabled: Boolean = true
    override val drawer: Drawer = GlDrawer
    @set:JvmName("_setScene")
    override var scene: Scene = EmptyScene
    override var isCullEnabled: Boolean = true

    override fun bindShader(shader: ShaderInstance) {
        this.shader = shader
        shader.bind()
    }

    override fun bindTexture(id: Int) {
        glBindTexture(GL_TEXTURE_2D, id)
    }

    override fun beginFrame() {
        GlApplication.window.beginFrame()

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        resetViewport()
    }

    override fun endFrame() {
        shader.unbind()
        shader = EmptyShaderInstance
        GlApplication.window.endFrame()
    }

    override fun setScene(scene: Scene) {
        this.scene = scene
    }

    override fun enableDepthTest() {
        isDepthTestEnabled = true
        glEnable(GL_DEPTH_TEST)
    }

    override fun disableDepthTest() {
        isDepthTestEnabled = false
        glDisable(GL_DEPTH_TEST)
    }

    override fun enableCull() {
        isCullEnabled = true
        glEnable(GL_CULL_FACE)
    }

    override fun disableCull() {
        isCullEnabled = false
        glDisable(GL_CULL_FACE)
    }

    override fun enableBlend() {
        isBlendEnabled = true
        glEnable(GL_BLEND)
    }

    override fun disableBlend() {
        isBlendEnabled = false
        glDisable(GL_BLEND)
    }


    override fun polygonMode(face: Int, mode: Int) {
        glPolygonMode(face, mode)
    }

    override fun setShaderColor(r: Float, g: Float, b: Float, a: Float) {
        glVertexAttrib4f(VertexFormatElement.COLOR.index, r, g, b, a)
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

    override fun translate(x: Float, y: Float, z: Float) {
        glTranslatef(x, y, z)
    }

    override fun rotate(angle: Float, x: Float, y: Float, z: Float) {
        glRotatef(angle, x, y, z)
    }

    override fun matrixMode(mode: Int) {
        glMatrixMode(mode)
    }

    override fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        glClearColor(red, green, blue, alpha)
    }

    override fun clear(mask: Int) {
        glClear(mask)
    }
}