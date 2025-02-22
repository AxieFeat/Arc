package arc

import arc.OS.execSafe
import org.lwjgl.glfw.GLFW
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File


abstract class AbstractApplication : Application {

    override var clipboardText: String
        get() {
            return GLFW.glfwGetClipboardString(window.handle) ?: ""
        }
        set(value) {
            GLFW.glfwSetClipboardString(window.handle, value)
        }

    override fun openURL(url: String) {
        Thread {
            when (platform.device.os) {
                OSPlatform.MACOS -> {
                    execSafe("open", url)
                }
                OSPlatform.LINUX -> {
                    execSafe("xdg-open", url)
                }
                OSPlatform.WINDOWS -> {
                    execSafe("rundll32", "url.dll,FileProtocolHandler", url)
                }

                else -> println("Can not open URL on this platform.")
            }
        }.also { it.isDaemon = true }.start()
    }

    override fun openFolder(folder: File) {
        Thread {
            when (platform.device.os) {
                OSPlatform.MACOS -> {
                    execSafe("open", folder.absolutePath)
                }
                OSPlatform.LINUX -> {
                    execSafe("xdg-open", folder.absolutePath)
                }
                OSPlatform.WINDOWS -> {
                    execSafe("explorer.exe /select," + folder.absolutePath.replace("/", "\\"))
                }

                else -> println("Can not open folder on this platform.")
            }
        }.also { it.isDaemon = true }.start()
    }

}