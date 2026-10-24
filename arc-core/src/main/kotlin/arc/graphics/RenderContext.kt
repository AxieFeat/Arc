package arc.graphics

import arc.shader.ShaderInstance
import arc.texture.Texture

/**
 * This interface represents the manager of rendering.
 */
interface RenderContext {

    /**
     * Current native-context generation, or zero when no tracked context is current.
     */
    val resourceGeneration: Long get() = 0L

    /**
     * Operations and limits supported by the current backend instance.
     */
    val capabilities: RenderCapabilities get() = RenderCapabilities()

    /**
     * Shader in the current context.
     */
    val shader: ShaderInstance

    /**
     * Texture in the current context.
     */
    val texture: Texture

    /**
     * Drawer of the render system.
     */
    val drawer: Drawer

    /**
     * Saves the current native graphics state without clearing or presenting. Close in LIFO order on the same thread.
     * Restores target, viewport/scissor, pipeline, textures/samplers, vertex/buffer bindings and raster state even
     * after drawing throws. Raw graphics work belongs inside this scope; native state is sampled once at entry.
     * Null [textureUnits] captures all units. Supplying a set reduces driver queries and declares every texture
     * unit the pass may modify; the previously active unit is also preserved. Arc texture binding uses unit zero.
     */
    fun beginScopedPass(textureUnits: Set<Int>? = null): AutoCloseable = unsupportedRenderOperation("Scoped render passes")

    /**
     * Restricts drawing to a target-pixel rectangle; null disables rectangular clipping.
     */
    fun setScissor(rectangle: PixelRect?): Unit = unsupportedRenderOperation("Scissor")

    /**
     * Replaces the current color and alpha blending configuration.
     */
    fun setBlend(state: BlendState): Unit = unsupportedRenderOperation("Typed blending")

    /**
     * Replaces depth testing and depth-write configuration.
     */
    fun setDepth(state: DepthState): Unit = unsupportedRenderOperation("Typed depth state")

    /**
     * Selects which polygon faces are discarded.
     */
    fun setCull(mode: CullMode): Unit = unsupportedRenderOperation("Typed culling")

    /**
     * Replaces polygon, sample and clip-distance state.
     */
    fun setRaster(state: RasterState): Unit = unsupportedRenderOperation("Typed raster state")

    /**
     * Selects which channels of the color attachment may be written.
     */
    fun setColorWrite(mask: ColorWriteMask) = colorMask(mask.red, mask.green, mask.blue, mask.alpha)

    /**
     * Replaces stencil testing and update operations for both faces.
     */
    fun setStencil(state: StencilState): Unit = unsupportedRenderOperation("Stencil masks")

    /**
     * Creates an owned sampler without modifying texture parameters.
     */
    fun createSampler(sampling: TextureSampling): TextureSampler = unsupportedRenderOperation("Texture samplers")

    /**
     * Binds a sampler to a texture unit; null restores texture-owned sampling parameters.
     */
    fun bindSampler(sampler: TextureSampler?, unit: Int = 0): Unit = unsupportedRenderOperation("Texture samplers")

    /**
     * Copies tightly packed top-to-bottom RGBA8 bytes. Caller retains the bytes; the returned texture is owned.
     */
    fun createRgbaTexture(width: Int, height: Int, rgba: ByteArray): Texture = unsupportedRenderOperation("RGBA textures")

    /**
     * Creates an owned RGBA8 target, optionally with a combined depth/stencil attachment.
     */
    fun createRenderTarget(width: Int, height: Int, depthStencil: Boolean = false): RenderTarget =
        unsupportedRenderOperation("Render targets")

    /**
     * Selects the target without clearing it. Null selects the default window framebuffer.
     */
    fun bindTarget(target: RenderTarget?): Unit = unsupportedRenderOperation("Render targets")

    /**
     * Explicitly clears the bound target, respecting scissor/write masks. Requested depth/stencil values are 1/0.
     */
    fun clearTarget(red: Float, green: Float, blue: Float, alpha: Float, depth: Boolean = false, stencil: Boolean = false): Unit =
        unsupportedRenderOperation("Typed target clear")

    /**
     * Bind specific shader in current context.
     *
     * @param shader Shader to bind.
     */
    fun bindShader(shader: ShaderInstance)

    /**
     * Bind specific texture in current context.
     *
     * @param texture Texture to bind.
     */
    fun bindTexture(texture: Texture)

    /**
     * Initializes a new rendering frame by preparing the rendering system.
     */
    fun beginFrame()

    /**
     * Completes the rendering process for the current frame.
     */
    fun endFrame()

    /**
     * Enable depth testing for the rendering system.
     */
    fun enableDepthTest()

    /**
     * Disable depth testing in the rendering system.
     */
    fun disableDepthTest()

    /**
     * Enable face culling in the rendering system.
     */
    fun enableCull()

    /**
     * Disable face culling in the rendering system.
     */
    fun disableCull()

    /**
     * Enable blending in the rendering system.
     */
    fun enableBlend()

    /**
     * Disable blending in the rendering system.
     */
    fun disableBlend()

    /**
     * Configures the color mask for rendering operations.
     *
     * @param red The red component of the color.
     * @param green The green component of the color.
     * @param blue The blue component of the color.
     * @param alpha The alpha (transparency) component of the color.
     */
    fun colorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean)

    /**
     * Configures the writability of the depth buffer in the rendering system.
     *
     * @param value Determines whether depth writing is enabled.
     */
    fun depthMask(value: Boolean)

    /**
     * Sets the dimensions and position of the viewport.
     *
     * @param x The x-coordinate of the lower-left corner of the viewport rectangle.
     * @param y The y-coordinate of the lower-left corner of the viewport rectangle.
     * @param width The width of the viewport rectangle.
     * @param height The height of the viewport rectangle.
     */
    fun setViewport(x: Int, y: Int, width: Int, height: Int)

    /**
     * Resets the viewport to its default state.
     */
    fun resetViewport()

    /**
     * Sets the clear color for the rendering system to use when clearing the screen.
     *
     * @param red The red component of the color in ``0.0..1.0``.
     * @param green The green component of the color in ``0.0..1.0``.
     * @param blue The blue component of the color in ``0.0..1.0``.
     * @param alpha The alpha (transparency) component of the color in ``0.0..1.0``.
     */
    fun clearColor(red: Float, green: Float, blue: Float, alpha: Float)

    /**
     * Clears the specified buffers in the rendering context.
     *
     * @param mask A bitfield indicating which buffers to clear. For example, it can include color, depth, or stencil buffers.
     */
    fun clear(mask: Int)

    /**
     * Enables 2D texture rendering in the rendering system.
     */
    fun enableTexture2D()

    /**
     * Disables 2D texture rendering in the rendering system.
     */
    fun disableTexture2D()

    /**
     * Configures the blending equation to determine how source and destination colors
     * are combined during a blending operation.
     */
    fun blendEquation(mode: Int)

    /**
     * Configures separate blending factors for color and alpha components during blending operations.
     *
     * @param sourceFactor The source factor for the color components.
     * @param destFactor The destination factor for the color components.
     * @param sourceFactorAlpha The source factor for the alpha components.
     * @param destFactorAlpha The destination factor for the alpha components.
     */
    fun blendFuncSeparate(sourceFactor: Int, destFactor: Int, sourceFactorAlpha: Int, destFactorAlpha: Int)

    /**
     * Configures the blending factors for source and destination colors during a blending operation.
     *
     * @param sourceFactor The source factor determines how the source color is scaled in the blending equation.
     * @param destFactor The destination factor determines how the destination color is scaled in the blending equation.
     */
    fun blendFunc(sourceFactor: Int, destFactor: Int)
}

private fun unsupportedRenderOperation(operation: String): Nothing =
    throw UnsupportedOperationException("$operation are unavailable in this render context")
