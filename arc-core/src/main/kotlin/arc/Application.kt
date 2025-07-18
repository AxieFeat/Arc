package arc

import arc.Arc.single
import arc.files.LocationSpace
import arc.graphics.RenderSystem
import arc.window.Window
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * This interface represents the Application of game engine.
 *
 * Sample of initializing application:
 * ```kotlin
 * // Preload factories.
 * ArcFactoryProvider.install()
 * ArcFactoryProvider.bootstrap()
 *
 * // Select implementation by property.
 * when(System.getProperty("arc.application")) {
 *     "opengl" -> OpenGL.preload()
 *     "vulkan" -> Vulkan.preload()
 *
 *     else -> OpenGL.preload() // Fallback to GL if property not correct.
 * }
 *
 * // Now we can find our application.
 * val application = Application.find()
 *
 * // Before any operations we need init all system via init().
 * application.init()
 * ```
 */
interface Application {

    /**
     * Provides access to information about backend implementation of application.
     */
    val backend: ApplicationBackend

    /**
     * Window of this application.
     */
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
     * Represents the current text stored in the system clipboard.
     */
    var clipboardText: String

    /**
     * Initializes the application.
     */
    fun init()

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
     * @throws IllegalStateException If provided [folder] is not directory.
     * @throws UnsupportedOperationException If any error occurs when trying to open a directory.
     */
    @Throws(IllegalStateException::class, UnsupportedOperationException::class)
    fun openFolder(folder: File)

    /**
     * Take a screenshot of screen.
     *
     * @param folder Folder, where save it.
     * @param name Name of screenshot (With extension).
     *
     * @throws IllegalStateException If screenshot with [name] already exist in [folder].
     */
    @Throws(IllegalStateException::class)
    fun screenshot(folder: File, name: String)

    /**
     * Terminates the application and releases all associated resources.
     */
    fun close()

    @ApiStatus.Internal
    interface Provider {

        fun provide(): Application

    }

    companion object {

        /**
         * Find specific implementation of [Application] in current context.
         *
         * @return Instance of [Application].
         */
        @JvmStatic
        fun find(): Application {
            return single<Provider>().provide()
        }

    }

}