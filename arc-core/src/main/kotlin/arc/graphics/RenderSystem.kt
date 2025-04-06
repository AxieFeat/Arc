package arc.graphics

import arc.shader.ShaderInstance
import arc.texture.Texture
import org.jetbrains.annotations.Range

/**
 * This interface represents manager of rendering.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface RenderSystem {

    /**
     * Shader in current context.
     */
    @get:JvmName("shader")
    val shader: ShaderInstance

    /**
     * Texture in current context.
     */
    @get:JvmName("texture")
    val texture: Texture

    /**
     * Is depth test enabled.
     */
    val isDepthTestEnabled: Boolean

    /**
     * Is culling enabled.
     */
    val isCullEnabled: Boolean

    /**
     * Is blending enabled.
     */
    val isBlendEnabled: Boolean

    /**
     * Drawer of render system.
     */
    @get:JvmName("drawer")
    val drawer: Drawer

    /**
     * Current game scene. Render scene represents all viewable objects in screen.
     */
    @get:JvmName("scene")
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
     * Sets the current color for the shader in the rendering pipeline.
     *
     * @param r The red component of the color.
     * @param g The green component of the color.
     * @param b The blue component of the color.
     * @param a The alpha (transparency) component of the color.
     */
    fun setShaderColor(
        r: @Range(from = 0, to = 1) Float,
        g: @Range(from = 0, to = 1) Float,
        b: @Range(from = 0, to = 1) Float,
        a: @Range(from = 0, to = 1) Float
    )

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

    fun clearColor(red: Float, green: Float, blue: Float, alpha: Float)

    fun clear(mask: Int)

    fun enableTexture2D()

    fun disableTexture2D()

    fun enableLighting()

    fun disableLighting()

    fun enableAlpha()

    fun disableAlpha()

    fun blendEquation(mode: Int)

    fun blendFuncSeparate(sourceFactor: Int, destFactor: Int, sourceFactorAlpha: Int, destFactorAlpha: Int)

    fun blendFunc(sourceFactor: Int, destFactor: Int)
}