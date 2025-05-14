package arc

import arc.annotations.TypeFactory
import arc.files.LocationSpace
import arc.graphics.RenderSystem
import arc.input.keyboard.KeyboardInput
import arc.input.mouse.MouseInput
import arc.window.Window
import arc.window.WindowException
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * This interface represents Application of game engine.
 *
 * Sample of initializing application:
 * ```kotlin
 * // Preload factories.
 * ArcFactoryProvider.install()
 * ArcFactoryProvider.bootstrap()
 *
 * // Select implementation by property.
 * when(System.getProperty("arc.application")) {
 *     "opengl" -> GlApplication.preload()
 *     "vulkan" -> VkApplication.preload()
 *
 *     else -> GlApplication.preload() // Fallback to GL if property not correct.
 * }
 *
 * // Now we can find our application.
 * val application = Application.find()
 *
 * // Before any operations we need init all system via init().
 * application.init(
 *     Configuration.create() // Here you can set configuration of application.
 * )
 * ```
 */
interface Application {

    /**
     * Provides access to platform-specific information and capabilities
     * for the application, such as device and OS details.
     */
    val platform: Platform

    /**
     * Window of this application.
     *
     * @throws WindowException If window is not initialized yet.
     */
    @get:Throws(WindowException::class)
    val window: Window

    /**
     * Render system of application.
     */
    val renderSystem: RenderSystem

    /**
     * Location space of application.
     */
    val locationSpace: LocationSpace

    /**
     * Mouse input device of this application.
     */
    val mouse: MouseInput

    /**
     * Keyboard input device of this application.
     */
    val keyboard: KeyboardInput

    /**
     * Represents the current text stored in the system clipboard.
     */
    var clipboardText: String

    /**
     * Initializes the application with the given configuration.
     *
     * @param configuration The configuration object that defines properties for the application's setup.
     */
    fun init(configuration: Configuration)

    /**
     * Opens the specified URL.
     *
     * @param url The URL to be opened.
     *
     * @throws UnsupportedOperationException If any error occurs when trying to open a URL.
     */
    @Throws(UnsupportedOperationException::class)
    fun openURL(url: String)

    /**
     * Opens the specified folder in the file explorer.
     *
     * @param folder The folder to be opened. It must be a valid directory.
     *
     * @throws IllegalArgumentException If provided [folder] is not directory.
     * @throws UnsupportedOperationException If any error occurs when trying to open a directory.
     */
    @Throws(IllegalArgumentException::class, UnsupportedOperationException::class)
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
     */
    fun close()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(): Application

    }

    companion object {

        /**
         * Find specific implementation of [Application] in current context.
         *
         * @return Instance of [Application].
         */
        @JvmStatic
        fun find(): Application {
            return Arc.factory<Factory>().create()
        }

    }

}