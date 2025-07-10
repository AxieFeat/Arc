package arc

import arc.OS.execSafe
import arc.files.SimpleLocationSpace
import arc.files.LocationSpace
import org.lwjgl.glfw.GLFW
import java.io.File

abstract class AbstractApplication : Application {

    override val locationSpace: LocationSpace = SimpleLocationSpace

    override var clipboardText: String
        get() {
            return GLFW.glfwGetClipboardString(window.handle) ?: ""
        }
        set(value) {
            GLFW.glfwSetClipboardString(window.handle, value)
        }

    override fun openURL(url: String) {
        Thread {
            val result = when (backend.device.os) {
                OSPlatform.MACOS -> execSafe("open", url)
                OSPlatform.LINUX -> execSafe("xdg-open", url)
                OSPlatform.WINDOWS -> execSafe("rundll32", "url.dll,FileProtocolHandler", url)

                else -> throw UnsupportedOperationException("Can not open URL on this platform.")
            }

            if(!result) throw UnsupportedOperationException("Occurred error in the opening URL.")
        }.also { it.isDaemon = true }.start()
    }

    override fun openFolder(folder: File) {
        check(folder.isDirectory) { "File is not a directory." }

        Thread {
            val result = when (backend.device.os) {
                OSPlatform.MACOS -> execSafe("open", folder.absolutePath)
                OSPlatform.LINUX -> execSafe("xdg-open", folder.absolutePath)
                OSPlatform.WINDOWS -> execSafe("explorer.exe /select," + folder.absolutePath.replace("/", "\\"))

                else -> throw UnsupportedOperationException("Can not open folder on this platform.")
            }

            if(!result) throw UnsupportedOperationException("Occurred error in the opening folder.")
        }.also { it.isDaemon = true }.start()
    }

}