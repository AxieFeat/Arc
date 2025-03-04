package arc

import arc.OS.execSafe
import arc.input.keyboard.ArcKeyboardInput
import arc.input.keyboard.KeyboardInput
import arc.input.mouse.ArcMouseInput
import arc.input.mouse.MouseInput
import org.lwjgl.glfw.GLFW
import java.io.File

abstract class AbstractApplication : Application {

    override var clipboardText: String
        get() {
            return GLFW.glfwGetClipboardString(window.handle) ?: ""
        }
        set(value) {
            GLFW.glfwSetClipboardString(window.handle, value)
        }

    override val mouse: MouseInput = ArcMouseInput
    override val keyboard: KeyboardInput = ArcKeyboardInput

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