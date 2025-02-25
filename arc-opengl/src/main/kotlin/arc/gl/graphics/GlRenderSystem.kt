package arc.gl.graphics

import arc.gl.GlApplication
import arc.graphics.*
import arc.graphics.vertex.VertexFormatElement
import org.lwjgl.opengl.GL41

internal object GlRenderSystem : RenderSystem {

    override var shader: ShaderInstance = EmptyShaderInstance

    override var isDepthTestEnabled: Boolean = true
    override var isBlendEnabled: Boolean = true
    override val drawer: Drawer = GlDrawer
    override var scene: Scene = EmptyScene
    override var isCullEnabled: Boolean = true

    override fun bindShader(shader: ShaderInstance) {
        this.shader = shader
        shader.bind()
    }

    override fun bindTexture(id: Int, texture: Texture) {

    }

    override fun beginFrame() {
        shader = EmptyShaderInstance
    }

    override fun endFrame() {
        shader.unbind()
        shader = EmptyShaderInstance
    }

    override fun setScene(scene: Scene) {
        this.scene = scene
    }

    override fun enableDepthTest() {
        isDepthTestEnabled = true
        GL41.glEnable(GL41.GL_DEPTH_TEST)
    }

    override fun disableDepthTest() {
        isDepthTestEnabled = false
        GL41.glDisable(GL41.GL_DEPTH_TEST)
    }

    override fun enableCull() {
        isCullEnabled = true
        GL41.glEnable(GL41.GL_CULL_FACE)
    }

    override fun disableCull() {
        isCullEnabled = false
        GL41.glDisable(GL41.GL_CULL_FACE)
    }

    override fun enableBlend() {
        isBlendEnabled = true
        GL41.glEnable(GL41.GL_BLEND)
    }

    override fun disableBlend() {
        isBlendEnabled = false
        GL41.glDisable(GL41.GL_BLEND)
    }


    override fun polygonMode(face: Int, mode: Int) {
        GL41.glPolygonMode(face, mode)
    }

    override fun setShaderColor(r: Float, g: Float, b: Float, a: Float) {
        GL41.glVertexAttrib4f(VertexFormatElement.COLOR.index, r, g, b, a)
    }

    override fun clearDepth(depth: Double) {
        GL41.glClearDepth(depth)
    }

    override fun colorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean) {
        GL41.glColorMask(red, green, blue, alpha)
    }

    override fun depthMask(value: Boolean) {
        GL41.glDepthMask(value)
    }

    override fun setViewport(x: Int, y: Int, width: Int, height: Int) {
        GL41.glViewport(x, y, width, height)
    }

    override fun resetViewport() {
        setViewport(0, 0, GlApplication.window.width, GlApplication.window.height)
    }
}