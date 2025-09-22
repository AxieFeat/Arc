package arc.vk

import arc.AbstractApplication
import arc.Application
import arc.ApplicationBackend
import arc.graphics.RenderSystem
import arc.vk.device.VulkanPhysicalDevice
import arc.vk.graphics.VkRenderSystem
import arc.vk.window.GlfwVkWindow
import arc.window.EmptyWindowHandler
import arc.window.Window
import arc.window.WindowException
import java.io.File

internal object VkApplication : AbstractApplication() {

    override val backend: ApplicationBackend = VkApplicationBackend

    private var _window: GlfwVkWindow? = null
    private var _renderSystem: RenderSystem? = null

    override val window: GlfwVkWindow
        get() = checkNotNull(_window) {
            "Window is not initialized yet. Accessing it before Application.init() is not allowed." }
    override val renderSystem: RenderSystem
        get() = checkNotNull(_renderSystem) {
            "RenderSystem is not initialized yet. Accessing it before Application.init() is not allowed."
        }

    private var _instance: VulkanInstance? = null
    private var _physicalDevice: VulkanPhysicalDevice? = null

    val instance: VulkanInstance
        get() = checkNotNull(_instance) {
            "VulkanInstance is not initialized yet. Accessing it before Application.init() is not allowed."
        }
    val physicalDevice: VulkanPhysicalDevice
        get() = checkNotNull(_physicalDevice) {
            "VulkanPhysicalDevice is not initialized yet. Accessing it before Application.init() is not allowed."
        }

    override fun init() {
        _window = Window.of(
            name = "",
            handler = EmptyWindowHandler,
            width = 720,
            height = 420
        ) as? GlfwVkWindow ?: throw WindowException("Window is not GlfwVkWindow. Why?")

        _instance = VulkanInstance()
        _physicalDevice = VulkanPhysicalDevice.createPhysicalDevice(instance)

        _renderSystem = VkRenderSystem

        window.create()
    }

    @Suppress("EmptyFunctionBlock") // TODO Remove when function will be implemented.
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
