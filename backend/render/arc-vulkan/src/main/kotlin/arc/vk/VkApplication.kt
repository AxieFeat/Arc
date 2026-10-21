package arc.vk

import arc.AbstractApplication
import arc.Application
import arc.ApplicationBackend
import arc.graphics.RenderContext
import arc.vk.device.VulkanPhysicalDevice
import arc.vk.graphics.VkRenderContext
import arc.vk.window.GlfwVkWindow
import arc.window.EmptyWindowHandler
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File

internal object VkApplication : AbstractApplication() {

    override val logger: KLogger = KotlinLogging.logger {}

    override val backend: ApplicationBackend = VkApplicationBackend

    private var _window: GlfwVkWindow? = null
    private var _renderContext: RenderContext? = null

    override val window: GlfwVkWindow
        get() = checkNotNull(_window) {
            "Window is not initialized yet. Accessing it before Application.init() is not allowed." }
    override val renderContext: RenderContext
        get() = checkNotNull(_renderContext) {
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
        super.init()

        _window = GlfwVkWindow(
            name = "",
            handler = EmptyWindowHandler,
            width = 720,
            height = 420,
            isResizable = true
        )

        _instance = VulkanInstance()
        _physicalDevice = VulkanPhysicalDevice.createPhysicalDevice(instance)

        _renderContext = VkRenderContext

        window.create()

        printStartedMessage()
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
