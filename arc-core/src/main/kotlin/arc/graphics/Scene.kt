package arc.graphics

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.graphics.g3d.ChunkContainer
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a mutable scene within the application. A scene typically contains a camera,
 * graphical elements, and supports updates and rendering during the rendering process.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Scene {

    /**
     * Represents the camera object associated with the scene. The camera is used to define
     * the viewing perspective and controls the rendering viewpoint within the scene. It is
     * an integral part of the scene for determining what portion of the environment is visible
     * during rendering.
     */
    @get:JvmName("camera")
    val camera: Camera

    /**
     * Represents the frames per second (FPS) value for the scene.
     *
     * This property defines the target or current framerate of the scene,
     * which is used to manage the update and render cycles. A higher FPS
     * indicates a smoother and more responsive visual experience, while a
     * lower FPS may result in choppier animations or delays in updates.
     *
     * The FPS value is typically utilized in performance tuning, frame
     * timing calculations, and ensuring consistent behavior in animations
     * and physics simulations.
     */
    @get:JvmName("fps")
    val fps: Int

    /**
     * Represents the time elapsed since the last frame in seconds.
     *
     * This variable is commonly used to perform time-based calculations in a scene,
     * such as animations, movements, or physics updates. The value of `delta`
     * is typically updated for every rendering cycle or frame, providing a precise
     * measure of the frame duration.
     */
    @get:JvmName("delta")
    val delta: Float

    /**
     * Indicates whether the scene is currently active and in use.
     *
     * This property is used to determine if the scene is actively being
     * updated or rendered within the application. If `true`, the scene
     * is marked as currently in use; otherwise, it is considered inactive.
     */
    @get:JvmName("inUse")
    val inUse: Boolean

    /**
     * Determines whether the rendering process for the scene should be skipped.
     *
     * When set to `true`, the `render()` method will not actively draw or update the visual
     * representation of the scene. This can be used to improve performance or temporarily
     * pause rendering operations, for instance, during background processing or when
     * rendering is unnecessary. If `false`, the rendering operations will proceed as normal.
     */
    var isSkipRender: Boolean

    /**
     * Controls the visibility of the cursor within the scene.
     *
     * If `true`, the cursor will be visible within the scene; if `false`,
     * the cursor will be hidden. This property is often used to manage
     * cursor visibility in various scenarios, such as when interacting with
     * UI elements or during specific camera manipulations.
     */
    @get:JvmName("showCursor")
    var showCursor: Boolean

    /**
     * Represents the global container of chunk sections within the scene.
     *
     * The `chunkContainer` allows dynamic management of chunk sections, where sections are
     * automatically generated upon retrieval. It provides access and facilitates updates to
     * individual chunk sections based on their 3D offsets. This container supports infinite
     * expansion, making it suitable for dynamic environments that require real-time chunk
     * management.
     */
    @get:JvmName("chunkContainer")
    val chunkContainer: ChunkContainer

    /**
     * Renders the current state of the scene.
     *
     * This method is responsible for handling all rendering operations for the scene,
     * including the visual representation of various components, camera adjustments,
     * and other graphical elements. It ensures that the scene is drawn to the screen
     * in its current state. The rendering context and order may depend on the state
     * of the scene, such as active elements, visibility settings, and interactions.
     */
    fun render()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Creates and returns a new instance of a `Scene`. Each scene is a self-contained environment
         * that typically includes elements such as a camera, graphical components, and associated
         * update and rendering logic. The created scene can be dynamically managed and utilized
         * within the context of an application to represent distinct graphical states or levels.
         *
         * @return A newly created `Scene` instance ready for use.
         */
        fun create(): Scene

    }

    companion object {

        /**
         * Creates and returns a new instance of a `Scene`. Each scene is a self-contained environment
         * that typically includes elements such as a camera, graphical components, and associated
         * update and rendering logic. The created scene can be dynamically managed and utilized
         * within the context of an application to represent distinct graphical states or levels.
         *
         * @return A newly created `Scene` instance ready for use.
         */
        @JvmStatic
        fun create(): Scene = Arc.factory<Factory>().create()

    }

}