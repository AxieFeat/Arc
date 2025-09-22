package arc.graphics

import arc.graphics.scene.Scene
import arc.shader.ShaderInstance
import arc.texture.Texture

/**
 * This interface represents the manager of rendering.
 */
interface RenderSystem {

    /**
     * Shader in the current context.
     */
    val shader: ShaderInstance

    /**
     * Texture in current context.
     */
    val texture: Texture

    /**
     * Drawer of a render system.
     */
    val drawer: Drawer

    /**
     * Current game scene. Render scene represents all viewable objects in the screen.
     */
    val scene: Scene

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
     * Sets the current scene to the provided scene object.
     *
     * @param scene The scene to be set as the current scene.
     */
    fun setScene(scene: Scene)

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
     * Sets the polygon rasterization mode for rasterizing polygons in the rendering pipeline.
     *
     * @param face Specifies which face(s) of the polygons the mode applies to.
     * @param mode Specifies the drawing mode to use for the polygons.
     */
    fun polygonMode(face: Int, mode: Int)

    /**
     * Clears the depth buffer to the specified depth value.
     *
     * @param depth The depth value to clear the depth buffer to.
     */
    fun clearDepth(depth: Double)

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
