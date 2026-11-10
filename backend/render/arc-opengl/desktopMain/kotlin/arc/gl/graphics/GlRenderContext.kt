package arc.gl.graphics

import arc.gl.GlApplication
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
import org.lwjgl.opengl.ARBImaging.glBlendEquation
import org.lwjgl.opengl.GL11.GL_BACK
import org.lwjgl.opengl.GL11.GL_BLEND
import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_COLOR_LOGIC_OP
import org.lwjgl.opengl.GL11.GL_CULL_FACE
import org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_DEPTH_TEST
import org.lwjgl.opengl.GL11.GL_FILL
import org.lwjgl.opengl.GL11.GL_FRONT
import org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK
import org.lwjgl.opengl.GL11.GL_LINE
import org.lwjgl.opengl.GL11.GL_MAX_TEXTURE_SIZE
import org.lwjgl.opengl.GL11.GL_POINT
import org.lwjgl.opengl.GL11.GL_SCISSOR_TEST
import org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_STENCIL_TEST
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.glBlendFunc
import org.lwjgl.opengl.GL11.glClear
import org.lwjgl.opengl.GL11.glClearColor
import org.lwjgl.opengl.GL11.glClearDepth
import org.lwjgl.opengl.GL11.glClearStencil
import org.lwjgl.opengl.GL11.glColorMask
import org.lwjgl.opengl.GL11.glCullFace
import org.lwjgl.opengl.GL11.glDepthFunc
import org.lwjgl.opengl.GL11.glDepthMask
import org.lwjgl.opengl.GL11.glDisable
import org.lwjgl.opengl.GL11.glEnable
import org.lwjgl.opengl.GL11.glGetInteger
import org.lwjgl.opengl.GL11.glPolygonMode
import org.lwjgl.opengl.GL11.glScissor
import org.lwjgl.opengl.GL11.glStencilFunc
import org.lwjgl.opengl.GL11.glStencilMask
import org.lwjgl.opengl.GL11.glStencilOp
import org.lwjgl.opengl.GL11.glViewport
import org.lwjgl.opengl.GL13.GL_SAMPLE_ALPHA_TO_COVERAGE
import org.lwjgl.opengl.GL13.GL_SAMPLE_COVERAGE
import org.lwjgl.opengl.GL14.glBlendFuncSeparate
import org.lwjgl.opengl.GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS
import org.lwjgl.opengl.GL20.glBlendEquationSeparate
import org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER
import org.lwjgl.opengl.GL30.GL_MAX_CLIP_DISTANCES
import org.lwjgl.opengl.GL30.GL_RASTERIZER_DISCARD
import org.lwjgl.opengl.GL30.glBindFramebuffer
import org.lwjgl.opengl.GL32.GL_SAMPLE_MASK
import org.lwjgl.opengl.GL33.glBindSampler

internal object GlRenderContext : RenderContext {

    override val resourceGeneration get() = GlContextIdentity.current()

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

    override val drawer: Drawer = GlDrawer

    private var clipGeneration = 0L
    private var clipCount = 0

    override fun beginScopedPass(textureUnits: Set<Int>?): AutoCloseable {
        val owner = Thread.currentThread()
        val generation = resourceGeneration
        check(generation != 0L) { "A graphics context must be current before opening a render pass" }
        val savedShader = shader
        val savedTexture = texture
        val state = GlRenderStateSnapshot(textureUnits)
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
        glToggle(GL_SCISSOR_TEST, rectangle != null)
        rectangle?.let { glScissor(it.x, it.y, it.width, it.height) }
    }

    override fun setRaster(state: RasterState) {
        if (clipGeneration != resourceGeneration) {
            check(resourceGeneration != 0L) { "An OpenGL context must be current" }
            clipCount = glGetInteger(GL_MAX_CLIP_DISTANCES)
            clipGeneration = resourceGeneration
        }
        require(state.clipDistances.all { it < clipCount }) { "Clip distance exceeds the context limit $clipCount" }
        glPolygonMode(
            GL_FRONT_AND_BACK, when (state.polygonMode) {
                PolygonMode.FILL -> GL_FILL
                PolygonMode.LINE -> GL_LINE
                PolygonMode.POINT -> GL_POINT
            }
        )
        glToggle(GL_RASTERIZER_DISCARD, state.discard)
        glToggle(GL_COLOR_LOGIC_OP, state.logicOperation)
        glToggle(GL_SAMPLE_ALPHA_TO_COVERAGE, state.alphaToCoverage)
        glToggle(GL_SAMPLE_COVERAGE, state.sampleCoverage)
        glToggle(GL_SAMPLE_MASK, state.sampleMask)
        for (index in 0 until clipCount) glToggle(GL_CLIP_DISTANCE0 + index, index in state.clipDistances)
    }

    override fun setBlend(state: BlendState) {
        glToggle(GL_BLEND, state.enabled)
        glBlendFuncSeparate(
            state.sourceColor.glValue(), state.destinationColor.glValue(),
            state.sourceAlpha.glValue(), state.destinationAlpha.glValue()
        )
        glBlendEquationSeparate(state.colorEquation.glValue(), state.alphaEquation.glValue())
    }

    override fun setDepth(state: DepthState) {
        glToggle(GL_DEPTH_TEST, state.testEnabled)
        glDepthMask(state.writeEnabled)
        glDepthFunc(state.comparison.glValue())
    }

    override fun setCull(mode: CullMode) {
        glToggle(GL_CULL_FACE, mode != CullMode.NONE)
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
        glToggle(GL_STENCIL_TEST, state.enabled)
        glStencilFunc(state.comparison.glValue(), state.reference, state.compareMask)
        glStencilMask(state.writeMask)
        glStencilOp(state.stencilFail.glValue(), state.depthFail.glValue(), state.pass.glValue())
    }

    override fun createRenderTarget(width: Int, height: Int, depthStencil: Boolean): RenderTarget =
        GlRenderTarget(width, height, depthStencil)

    override fun createSampler(sampling: TextureSampling): TextureSampler = GlTextureSampler(sampling)

    override fun createRgbaTexture(width: Int, height: Int, rgba: ByteArray): Texture = GlRgbaTexture(width, height, rgba)

    override fun bindSampler(sampler: TextureSampler?, unit: Int) {
        require(sampler == null || sampler is GlTextureSampler) { "Sampler belongs to a different backend" }
        require(unit in 0 until textureUnitLimit) { "Invalid texture unit $unit" }
        glBindSampler(unit, sampler?.let { (it as GlTextureSampler).checkedId() } ?: 0)
    }

    override fun bindTarget(target: RenderTarget?) {
        require(target == null || target is GlRenderTarget) { "Render target belongs to a different backend" }
        if (target == null) glBindFramebuffer(GL_FRAMEBUFFER, 0)
        else target.bind()
    }

    override fun clearTarget(red: Float, green: Float, blue: Float, alpha: Float, depth: Boolean, stencil: Boolean) {
        clearColor(red, green, blue, alpha)
        if (depth) glClearDepth(1.0)
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
        GlApplication.window.pollEvents()

        clear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        resetViewport()
    }

    override fun endFrame() {
        shader.unbind()
        shader = EmptyShaderInstance
        texture.unbind()
        texture = EmptyTexture
        GlApplication.window.swapBuffers()
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
