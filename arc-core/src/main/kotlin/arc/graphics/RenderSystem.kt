package arc.graphics

import arc.annotations.MutableType
import arc.shader.ShaderInstance
import arc.texture.Texture
import arc.texture.TextureLike
import org.jetbrains.annotations.Range

/**
 * A rendering system interface that defines operations and properties for managing the
 * 3D rendering pipeline. It provides methods for handling shaders, textures, and render
 * state management, enabling users to control various aspects of the rendering process.
 *
 * This interface is designed to be implemented by any rendering framework or engine that
 * supports modern rendering techniques like shaders, blending, and depth testing.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface RenderSystem {

    /**
     * Represents the current shader instance used by the `RenderSystem`.
     *
     * The shader is a critical component in the rendering pipeline, responsible
     * for combining vertex and fragment shaders to produce visual effects. This
     * property holds the shader instance that is currently active and will be
     * used for rendering operations. It should be managed using appropriate
     * methods like `bindShader` and `unbind` within the `RenderSystem`.
     */
    @get:JvmName("shader")
    val shader: ShaderInstance

    @get:JvmName("texture")
    val texture: Texture

    /**
     * Indicates whether depth testing is enabled in the rendering system.
     *
     * Depth testing is a technique used in 3D rendering to ensure that
     * objects closer to the camera are rendered in front of objects
     * farther away. When enabled, the depth buffer is used to compare
     * the depth values of pixels and determine their visibility.
     *
     * This property reflects the current state of depth testing
     * and can be modified using methods like `enableDepthTest()` or
     * `disableDepthTest()` in the rendering system.
     */
    val isDepthTestEnabled: Boolean

    /**
     * Indicates whether culling is enabled in the rendering system.
     * Culling is the process of discarding certain faces of geometry
     * (usually back faces) to improve rendering performance and avoid
     * rendering unseen surfaces.
     */
    val isCullEnabled: Boolean

    /**
     * Indicates whether blending is enabled in the rendering system.
     * When enabled, blending allows the combination of colors from multiple
     * rendered fragments, commonly used for effects such as transparency.
     */
    val isBlendEnabled: Boolean

    /**
     * Represents the graphical drawer system used for initiating and managing rendering operations.
     * Provides capabilities to define drawing primitives and vertex formats for rendering graphical elements.
     */
    @get:JvmName("drawer")
    val drawer: Drawer

    /**
     * Current game scene. Render scene represents all viewable objects in screen.
     */
    @get:JvmName("scene")
    val scene: Scene

    /**
     * Binds the specified shader instance for rendering operations.
     *
     * This method activates the given shader, making it the one in use
     * for subsequent rendering processes. The provided shader must
     * be compiled before being bound.
     *
     * @param shader The shader instance to bind. This instance should
     * have compiled vertex and fragment shaders ready for use in the
     * rendering pipeline.
     */
    fun bindShader(shader: ShaderInstance)

    /**
     * Binds the specified texture to the rendering system using the provided identifier.
     */
    fun bindTexture(texture: Texture)

    /**
     * Initializes a new rendering frame by preparing the rendering system.
     *
     * This method is typically called at the beginning of a frame to set up necessary
     * states for rendering operations. It may reset or configure internal
     * render settings to ensure a consistent starting point for the frame.
     */
    fun beginFrame()

    /**
     * Completes the rendering process for the current frame.
     *
     * This method finalizes all rendering operations for the current frame. It is
     * typically called after all rendering commands have been issued to ensure that
     * the state of the rendering pipeline is properly resolved and ready for the
     * next frame.
     *
     * Responsible for syncing any pending GPU operations, resetting internal
     * rendering states, and potentially presenting the frame to the screen
     * depending on the rendering context.
     */
    fun endFrame()

    /**
     * Sets the current scene to the provided scene object.
     *
     * @param scene The scene to be set as the current scene.
     */
    fun setScene(scene: Scene)

    /**
     * Enables depth testing for the rendering system.
     *
     * Depth testing is used to determine the visibility of objects in a 3D environment
     * based on their depth values. When enabled, fragments closer to the camera will
     * overwrite fragments that are further away, ensuring proper occlusion and rendering
     * order.
     *
     * This method modifies the rendering system's current state to activate depth testing,
     * which is essential for rendering scenes with correct depth perception.
     */
    fun enableDepthTest()

    /**
     * Disables depth testing in the rendering system.
     *
     * Depth testing is used to determine the visibility of objects based on their depth values
     * and ensures proper occlusion in 3D rendering by comparing depth values of overlapping fragments.
     *
     * When this method is called, the depth testing feature is turned off, allowing fragments to be
     * rendered without considering their depth values. This can be useful for rendering elements
     * like overlays or UI components where depth-based visibility is not required.
     */
    fun disableDepthTest()

    /**
     * Enables face culling in the rendering system.
     *
     * Face culling is a technique used to improve rendering efficiency by excluding
     * surfaces (faces) of objects that are not visible from the current viewpoint.
     * This method activates the culling mechanism, typically discarding back-facing
     * polygons during rendering.
     *
     * When enabled, objects will only render their visible faces, reducing the
     * number of polygons processed by the graphics pipeline. This can enhance
     * rendering performance, especially in complex scenes.
     */
    fun enableCull()

    /**
     * Disables face culling in the rendering system.
     *
     * Face culling is a technique used to improve rendering efficiency by discarding
     * surfaces of objects that are not visible from the current viewpoint. When culling
     * is disabled, all faces of objects, including those that would normally be culled
     * (e.g., back-facing polygons), are rendered.
     *
     * This method modifies the rendering system's current state to turn off face culling,
     * which can be useful in scenarios where all surfaces need to be visible, such as
     * when rendering transparent objects or debugging 3D geometry.
     */
    fun disableCull()

    /**
     * Enables blending in the rendering system.
     *
     * Blending is a technique used to combine the color of a newly rendered fragment
     * with the color of the fragment already present in the framebuffer. This is useful for
     * rendering transparent or semi-transparent objects by mixing their colors with the background.
     *
     * When this method is called, the rendering system adjusts its state to enable blending,
     * which allows for more advanced visual effects, such as transparency and anti-aliasing.
     */
    fun enableBlend()

    /**
     * Disables blending in the rendering system.
     *
     * Blending is a process used in rendering to combine colors of overlapping
     * fragments, typically for effects like transparency or anti-aliasing. When
     * blending is disabled, newly rendered fragments overwrite existing fragments
     * in the framebuffer without applying blending calculations.
     *
     * Calling this method updates the rendering system's state to stop blending
     * operations, which can be useful for improving performance when blending is
     * not required or for rendering opaque objects without transparency.
     */
    fun disableBlend()

    /**
     * Sets the polygon rasterization mode for rasterizing polygons in the rendering pipeline.
     *
     * This method controls how polygons are rasterized by specifying the face of the polygons
     * (e.g., front, back, or both) and the mode of rendering (e.g., points, lines, or filled).
     *
     * @param face Specifies which face(s) of the polygons the mode applies to. Acceptable values
     *             typically include FRONT, BACK, or FRONT_AND_BACK.
     * @param mode Specifies the drawing mode to use for the polygons. Common modes include POINT,
     *             LINE, or FILL, determining if the polygons are rendered as points, wireframe lines,
     *             or fully filled surfaces.
     */
    fun polygonMode(face: Int, mode: Int)

    /**
     * Sets the current color for the shader in the rendering pipeline.
     *
     * This method updates the RGBA color values used by the shader during rendering operations.
     * The specified values will influence the color or shading effects applied to rendered objects,
     * depending on the shader's implementation.
     *
     * @param r The red component of the color, represented as a floating-point value between 0.0 and 1.0.
     * @param g The green component of the color, represented as a floating-point value between 0.0 and 1.0.
     * @param b The blue component of the color, represented as a floating-point value between 0.0 and 1.0.
     * @param a The alpha (transparency) component of the color, represented as a floating-point value between 0.0 (completely transparent) and 1.0 (completely opaque).
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
     * This method sets the depth value for all pixels in the depth buffer, effectively resetting
     * the depth state in the rendering system. It is commonly used at the beginning of a rendering
     * frame or before rendering a new scene to ensure proper depth calculations for subsequent
     * rendering operations.
     *
     * @param depth The depth value to clear the depth buffer to. Typically, this value ranges
     *              between 0.0 (near plane) and 1.0 (far plane), depending on the depth range
     *              configuration of the rendering system.
     */
    fun clearDepth(depth: Double)

    /**
     * Configures the color mask for rendering operations.
     *
     * This method enables or disables writing of individual color components
     * (red, green, blue, and alpha) to the color buffer. When a component is
     * disabled, the corresponding output color will not be written during rendering.
     *
     * @param red   Whether the red component should be writable.
     * @param green Whether the green component should be writable.
     * @param blue  Whether the blue component should be writable.
     * @param alpha Whether the alpha component should be writable.
     */
    fun colorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean)

    /**
     * Configures the writability of the depth buffer in the rendering system.
     *
     * This method enables or disables writing to the depth buffer. When depth writing is enabled,
     * the depth values of rendered fragments are written into the depth buffer, which is crucial
     * for depth testing in 3D rendering. Disabling depth writing can be useful in scenarios
     * such as rendering transparent objects or performing post-processing effects.
     *
     * @param value Determines whether depth writing is enabled.
     *              Pass `true` to enable depth writing, or `false` to disable it.
     */
    fun depthMask(value: Boolean)

    /**
     * Sets the dimensions and position of the viewport.
     *
     * @param x The x-coordinate of the lower-left corner of the viewport rectangle in pixels.
     * @param y The y-coordinate of the lower-left corner of the viewport rectangle in pixels.
     * @param width The width of the viewport rectangle in pixels.
     * @param height The height of the viewport rectangle in pixels.
     */
    fun setViewport(x: Int, y: Int, width: Int, height: Int)

    /**
     * Resets the viewport to its default state.
     * This method can be used to restore the initial settings of the viewport,
     * clearing any customizations or transformations that might have been applied.
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
}