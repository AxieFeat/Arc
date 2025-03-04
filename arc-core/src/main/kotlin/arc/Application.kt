package arc

import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.files.LocationSpace
import arc.graphics.RenderSystem
import arc.input.InputDevice
import arc.input.controller.ControllerInput
import arc.input.keyboard.KeyboardInput
import arc.input.mouse.MouseInput
import arc.window.Window
import arc.window.WindowException
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * Represents an application interface. This interface provides access to various components
 * and functionalities such as the platform, window, input devices, rendering system, and more.
 *
 * It defines methods for initializing, interacting with external resources, and controlling
 * the application lifecycle.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Application {

    /**
     * Provides access to platform-specific information and capabilities
     * for the application, such as device and OS details.
     */
    @get:JvmName("platform")
    val platform: Platform

    /**
     * Provides access to the main application window.
     *
     * This property represents an instance of the `Window` interface, which contains methods and properties
     * for interacting with the application's window, such as resizing, setting handlers for window events,
     * and querying window attributes (e.g., position, size, and visibility).
     *
     * @throws WindowException If window is not initialized yet.
     */
    @get:Throws(WindowException::class)
    @get:JvmName("window")
    val window: Window

    /**
     * Represents the rendering system used by the application for managing
     * all rendering operations. Provides functionality such as binding shaders,
     * textures, managing frame lifecycle, enabling/disabling rendering options
     * like depth testing, culling, and blending, as well as viewport and color
     * settings.
     *
     * This property is a part of the application's major systems and allows
     * direct interaction with rendering capabilities.
     */
    @get:JvmName("renderSystem")
    val renderSystem: RenderSystem

    /**
     * Represents the file location management system for the application.
     * Provides utility functions to access files based on specific location contexts such as local, absolute, or classpath.
     * Used to manage and retrieve file paths relative to the application's directory or other predefined contexts.
     */
    @get:JvmName("locationSpace")
    val locationSpace: LocationSpace

    @get:JvmName("mouse")
    val mouse: MouseInput

    @get:JvmName("keyboard")
    val keyboard: KeyboardInput

    @get:JvmName("controllers")
    val controllers: List<ControllerInput>

    /**
     * Represents the current text stored in the system clipboard.
     *
     * This variable allows reading and modifying the clipboard content.
     * Modifying this property changes the clipboard text to the provided value.
     */
    @get:JvmName("clipboardText")
    var clipboardText: String

    /**
     * Initializes the application with the given configuration.
     *
     * @param configuration The configuration object that defines properties for the application's setup,
     * including window dimensions, title, refresh rate, and fullscreen mode.
     */
    fun init(configuration: Configuration)

    /**
     * Opens the specified URL.
     *
     * @param url The URL to be opened.
     */
    fun openURL(url: String)

    /**
     * Opens the specified folder in the file explorer.
     *
     * @param folder The folder to be opened. It must be a valid directory.
     */
    fun openFolder(folder: File)

    /**
     * Take screenshot of screen.
     *
     * @param folder Folder, where save it.
     * @param name Name of screenshot (With extension).
     *
     * @throws IllegalArgumentException If screenshot with [name] already exist in [folder].
     */
    @Throws(IllegalArgumentException::class)
    fun screenshot(folder: File, name: String)

    /**
     * Terminates the application and releases all associated resources.
     *
     * This method is responsible for shutting down the application, ensuring that
     * all systems, such as input devices, rendering, and window management, are
     * properly closed. After calling this method, the application should no
     * longer be used. This operation cannot be undone.
     */
    fun close()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Find specific implementation of [Application].
         *
         * @param impl Name of implementation. (e.g. opengl, vulkan)
         *
         * @return Instance of [Application].
         */
        fun find(impl: String): Application

    }

    companion object {

        /**
         * Find specific implementation of [Application].
         *
         * @param impl Name of implementation. (e.g. opengl, vulkan)
         *
         * @return Instance of [Application].
         */
        @JvmStatic
        fun find(impl: String): Application {
            return Arc.factory<Factory>().find(impl)
        }

    }

}