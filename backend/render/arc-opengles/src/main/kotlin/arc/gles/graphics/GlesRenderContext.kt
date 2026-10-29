package arc.gles.graphics

import arc.gles.GlesApplication
import arc.graphics.BlendState
import arc.graphics.ColorWriteMask
import arc.graphics.CullMode
import arc.graphics.DepthState
import arc.graphics.Drawer
import arc.graphics.EmptyShaderInstance
import arc.graphics.PixelRect
import arc.graphics.PolygonMode
import arc.graphics.RasterState
import arc.graphics.RenderCapabilities
import arc.graphics.RenderContext
import arc.graphics.RenderTarget
import arc.graphics.StencilState
import arc.graphics.TextureSampler
import arc.graphics.TextureSampling
import arc.shader.ShaderInstance
import arc.texture.EmptyTexture
import arc.texture.Texture
import org.lwjgl.opengles.GLES.getCapabilities
import org.lwjgl.opengles.GLES20.GL_BACK
import org.lwjgl.opengles.GLES20.GL_BLEND
import org.lwjgl.opengles.GLES20.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengles.GLES20.GL_CULL_FACE
import org.lwjgl.opengles.GLES20.GL_DEPTH_BUFFER_BIT
import org.lwjgl.opengles.GLES20.GL_DEPTH_TEST
import org.lwjgl.opengles.GLES20.GL_FRONT
import org.lwjgl.opengles.GLES20.GL_FRONT_AND_BACK
import org.lwjgl.opengles.GLES20.GL_MAX_TEXTURE_SIZE
import org.lwjgl.opengles.GLES20.GL_SAMPLE_ALPHA_TO_COVERAGE
import org.lwjgl.opengles.GLES20.GL_SAMPLE_COVERAGE
import org.lwjgl.opengles.GLES20.GL_SCISSOR_TEST
import org.lwjgl.opengles.GLES20.GL_STENCIL_BUFFER_BIT
import org.lwjgl.opengles.GLES20.GL_STENCIL_TEST
import org.lwjgl.opengles.GLES20.GL_TEXTURE_2D
import org.lwjgl.opengles.GLES20.glBlendEquation
import org.lwjgl.opengles.GLES20.glBlendFunc
import org.lwjgl.opengles.GLES20.glBlendFuncSeparate
import org.lwjgl.opengles.GLES20.glClear
import org.lwjgl.opengles.GLES20.glClearColor
import org.lwjgl.opengles.GLES20.glClearDepthf
import org.lwjgl.opengles.GLES20.glClearStencil
import org.lwjgl.opengles.GLES20.glColorMask
import org.lwjgl.opengles.GLES20.glCullFace
import org.lwjgl.opengles.GLES20.glDepthFunc
import org.lwjgl.opengles.GLES20.glDepthMask
import org.lwjgl.opengles.GLES20.glDisable
import org.lwjgl.opengles.GLES20.glEnable
import org.lwjgl.opengles.GLES20.glGetInteger
import org.lwjgl.opengles.GLES20.glScissor
import org.lwjgl.opengles.GLES20.glStencilFunc
import org.lwjgl.opengles.GLES20.glStencilMask
import org.lwjgl.opengles.GLES20.glStencilOp
import org.lwjgl.opengles.GLES20.glViewport
import org.lwjgl.opengles.GLES30
import org.lwjgl.opengles.GLES30.GL_FRAMEBUFFER
import org.lwjgl.opengles.GLES30.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS
import org.lwjgl.opengles.GLES30.GL_RASTERIZER_DISCARD
import org.lwjgl.opengles.GLES30.glBindFramebuffer
import org.lwjgl.opengles.GLES30.glBindSampler
import org.lwjgl.opengles.GLES30.glBlendEquationSeparate
import org.lwjgl.opengles.GLES31.GL_SAMPLE_MASK

internal object GlesRenderContext : RenderContext {

    override val resourceGeneration get() = GlesContextIdentity.current()

    private var capabilityGeneration = 0L
    private var maximumTextureDimension: Int? = null
    override val capabilities: RenderCapabilities
        get() {
            val generation = resourceGeneration
            if (generation != capabilityGeneration) {
                capabilityGeneration = generation
                maximumTextureDimension = if (generation == 0L) null else glGetInteger(GL_MAX_TEXTURE_SIZE)
            }
            return RenderCapabilities(
                scopedPass = true, scissor = true, typedState = true, renderTargets = true, stencilMasks = true,
                maxTextureDimension = maximumTextureDimension,
            )
        }
    private val passes = ArrayDeque<AutoCloseable>()
    private var textureUnitGeneration = 0L
    private var textureUnitCount = 0
    private val textureUnitLimit: Int
        get() {
            val generation = resourceGeneration
            check(generation != 0L) { "A graphics context must be current" }
            if (generation != textureUnitGeneration) {
                textureUnitCount = glGetInteger(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS)
                textureUnitGeneration = generation
            }
            return textureUnitCount
        }

    private var bindingsGeneration = 0L
    private var currentShader: ShaderInstance = EmptyShaderInstance
    private var currentTexture: Texture = EmptyTexture

    override var shader: ShaderInstance
        get() {
            synchronizeBindings()
            return currentShader
        }
        set(value) {
            synchronizeBindings()
            currentShader = value
        }
    override var texture: Texture
        get() {
            synchronizeBindings()
            return currentTexture
        }
        set(value) {
            synchronizeBindings()
            currentTexture = value
        }

    override val drawer: Drawer = GlesDrawer

    override fun beginScopedPass(textureUnits: Set<Int>?): AutoCloseable {
        val owner = Thread.currentThread()
        val generation = resourceGeneration
        check(generation != 0L) { "A graphics context must be current before opening a render pass" }
        val savedShader = shader
        val savedTexture = texture
        val state = GlesRenderStateSnapshot(textureUnits)
        var closed = false
        val pass = object : AutoCloseable {
            override fun close() {
                if (closed) return
                check(Thread.currentThread() === owner) { "A render pass must close on its creating thread" }
                check(passes.lastOrNull() === this) { "Render passes must close in reverse creation order" }
                try {
                    check(resourceGeneration == generation) { "The graphics context changed inside an open render pass; its snapshot was discarded" }
                    state.restore()
                    shader = savedShader
                    texture = savedTexture
                } finally {
                    passes.removeLast()
                    closed = true
                }
            }
        }
        passes.addLast(pass)
        return pass
    }

    override fun setScissor(rectangle: PixelRect?) {
        glesToggle(GL_SCISSOR_TEST, rectangle != null)
        rectangle?.let { glScissor(it.x, it.y, it.width, it.height) }
    }

    override fun setRaster(state: RasterState) {
        require(state.polygonMode == PolygonMode.FILL && !state.logicOperation && state.clipDistances.isEmpty()) {
            "GLES raster state supports filled polygons without color logic operations or user clip distances"
        }
        val supportsMask = getCapabilities().GLES31
        require(!state.sampleMask || supportsMask) { "Sample masks require GLES 3.1" }
        glesToggle(GL_RASTERIZER_DISCARD, state.discard)
        glesToggle(GL_SAMPLE_ALPHA_TO_COVERAGE, state.alphaToCoverage)
        glesToggle(GL_SAMPLE_COVERAGE, state.sampleCoverage)
        if (supportsMask) glesToggle(GL_SAMPLE_MASK, state.sampleMask)
    }

    override fun setBlend(state: BlendState) {
        glesToggle(GL_BLEND, state.enabled)
        GLES30.glBlendFuncSeparate(
            state.sourceColor.glesValue(), state.destinationColor.glesValue(),
            state.sourceAlpha.glesValue(), state.destinationAlpha.glesValue()
        )
        glBlendEquationSeparate(state.colorEquation.glesValue(), state.alphaEquation.glesValue())
    }

    override fun setDepth(state: DepthState) {
        glesToggle(GL_DEPTH_TEST, state.testEnabled)
        glDepthMask(state.writeEnabled)
        glDepthFunc(state.comparison.glesValue())
    }

    override fun setCull(mode: CullMode) {
        glesToggle(GL_CULL_FACE, mode != CullMode.NONE)
        if (mode != CullMode.NONE) glCullFace(
            when (mode) {
                CullMode.FRONT -> GL_FRONT
                CullMode.BACK -> GL_BACK
                CullMode.FRONT_AND_BACK -> GL_FRONT_AND_BACK
                CullMode.NONE -> error("Disabled culling has no face")
            }
        )
    }

    override fun setColorWrite(mask: ColorWriteMask) = colorMask(mask.red, mask.green, mask.blue, mask.alpha)

    override fun setStencil(state: StencilState) {
        glesToggle(GL_STENCIL_TEST, state.enabled)
        glStencilFunc(state.comparison.glesValue(), state.reference, state.compareMask)
        glStencilMask(state.writeMask)
        glStencilOp(state.stencilFail.glesValue(), state.depthFail.glesValue(), state.pass.glesValue())
    }

    override fun createRenderTarget(width: Int, height: Int, depthStencil: Boolean): RenderTarget =
        GlesRenderTarget(width, height, depthStencil)

    override fun createSampler(sampling: TextureSampling): TextureSampler = GlesTextureSampler(sampling)

    override fun createRgbaTexture(width: Int, height: Int, rgba: ByteArray): Texture = GlesRgbaTexture(width, height, rgba)

    override fun bindSampler(sampler: TextureSampler?, unit: Int) {
        require(sampler == null || sampler is GlesTextureSampler) { "Sampler belongs to a different backend" }
        require(unit in 0 until textureUnitLimit) { "Invalid texture unit $unit" }
        glBindSampler(unit, sampler?.let { (it as GlesTextureSampler).checkedId() } ?: 0)
    }

    override fun bindTarget(target: RenderTarget?) {
        require(target == null || target is GlesRenderTarget) { "Render target belongs to a different backend" }
        if (target == null) glBindFramebuffer(GL_FRAMEBUFFER, 0)
        else target.bind()
    }

    override fun clearTarget(red: Float, green: Float, blue: Float, alpha: Float, depth: Boolean, stencil: Boolean) {
        clearColor(red, green, blue, alpha)
        if (depth) glClearDepthf(1f)
        if (stencil) glClearStencil(0)
        glClear(
            GL_COLOR_BUFFER_BIT or (if (depth) GL_DEPTH_BUFFER_BIT else 0) or
                    (if (stencil) GL_STENCIL_BUFFER_BIT else 0)
        )
    }

    private fun synchronizeBindings() {
        val generation = resourceGeneration
        if (generation != bindingsGeneration) {
            bindingsGeneration = generation
            currentShader = EmptyShaderInstance
            currentTexture = EmptyTexture
        }
    }

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
