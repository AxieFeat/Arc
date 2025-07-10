package arc.vk

import arc.AbstractApplication
import arc.Application
import arc.ApplicationBackend
import arc.graphics.RenderSystem
import arc.vk.device.VulkanPhysicalDevice
import arc.vk.window.GlfwVkWindow
import arc.window.EmptyWindowHandler
import arc.window.Window
import java.io.File

internal object VkApplication : AbstractApplication() {

    override val backend: ApplicationBackend = VkApplicationBackend

    override lateinit var window: GlfwVkWindow
    override lateinit var renderSystem: RenderSystem

    lateinit var instance: VulkanInstance
    lateinit var physicalDevice: VulkanPhysicalDevice

    override fun init() {
        this.window = Window.of(
            name = "",
            handler = EmptyWindowHandler,
            height = 420,
            width = 720
        ) as? GlfwVkWindow ?: throw IllegalStateException("Window is not GlfwVkWindow. Why?")

        instance = VulkanInstance()
        physicalDevice = VulkanPhysicalDevice.createPhysicalDevice(instance)

        window.create()
    }

    override fun screenshot(folder: File, name: String) {

    }

    override fun close() {
        window.close()
    }

    internal object Provider : Application.Provider {
        override fun provide(): Application {
            return VkApplication
        }
    }

}