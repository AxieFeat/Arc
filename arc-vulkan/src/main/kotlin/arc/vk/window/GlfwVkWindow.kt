package arc.vk.window

import arc.vk.VkApplication
import arc.window.AbstractGlfwWindow
import arc.window.Window
import arc.window.WindowHandler
import org.lwjgl.glfw.GLFW.GLFW_CLIENT_API
import org.lwjgl.glfw.GLFW.GLFW_NO_API
import org.lwjgl.glfw.GLFW.glfwWindowHint
import org.lwjgl.glfw.GLFWVulkan
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.KHRSurface
import kotlin.properties.Delegates
import kotlin.use

internal class GlfwVkWindow(
    name: String,
    handler: WindowHandler,
    width: Int,
    height: Int,
    isResizable: Boolean
) : AbstractGlfwWindow(name, handler, width, height, isResizable) {

    var surface by Delegates.notNull<Long>()

    init {
        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API)
    }

    override fun create() {
        super.create()

        MemoryStack.stackPush().use { stack ->
            val pSurface = stack.mallocLong(1)
            GLFWVulkan.glfwCreateWindowSurface(
                VkApplication.physicalDevice.vkPhysicalDevice.instance, handle,
                null, pSurface
            )
            this.surface = pSurface.get(0)
        }
    }

    override fun close() {
        super.close()
        KHRSurface.vkDestroySurfaceKHR(VkApplication.physicalDevice.vkPhysicalDevice.instance, surface, null)
    }

    object Factory : Window.Factory {

        override fun create(name: String, handler: WindowHandler, width: Int, height: Int, isResizable: Boolean): Window {
            return GlfwVkWindow(name, handler, width, height, isResizable)
        }
    }
}
